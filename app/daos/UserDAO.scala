package daos

import model.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class UserDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                       (implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val Users = TableQuery[UserTable]

  def all(): Future[Seq[User]] = db.run(Users.result)

  def insert(user: User): Future[Unit] = {
    db.run(Users.insertOrUpdate(user)).map { _ => () }
  }


  private class UserTable(tag: Tag) extends Table[User](tag, "user") {

    def username = column[String]("username", O.PrimaryKey)
    def password = column[String]("password")

    def * = (username, password) <> (User.tupled, User.unapply)
  }
}