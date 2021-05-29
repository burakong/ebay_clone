package daos

import model.{Article, Produkt}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ArticleDAO  @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                           (implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val Articles = TableQuery[ArticleTable]

  def all(): Future[Seq[Article]] = db.run(Articles.result)

  def insert(article: Article): Future[Unit] = {
    db.run(Articles.insertOrUpdate(article)).map { _ => () }
  }
  def searchByName(name: String): Future[Seq[Article]] = db.run(
    Articles.filter(_.name === name)
      .result)

  private class ArticleTable(tag: Tag) extends Table[Article](tag, "article") {

    def name = column[String]("name", O.PrimaryKey)
    def price = column[Int]("price")
    def description = column[String]("description")

    def * = (name, price, description) <> (Article.tupled, Article.unapply)
  }

}
