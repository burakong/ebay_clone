package controllers

import daos.{ArticleDAO, ProduktDAO, UserDAO}
import model.{Article, Produkt, User}

import javax.inject._
import play.api._
import play.api.data.Form
import play.api.data.Forms.{mapping, number, text}
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class HomeController @Inject() (userDAO: UserDAO,produktDao: ProduktDAO,articleDAO: ArticleDAO ,controllerComponents: ControllerComponents)
                               (implicit executionContext: ExecutionContext) extends AbstractController(controllerComponents) {

  def index() = Action.async {
    userDAO.all().map { case (users) => Ok(views.html.index(users)) }
    //Ok(views.html.index(produktDao.all()))
  }
  def home() = Action.async {
    userDAO.all().map { case (users) => Ok(views.html.home(users)) }
    //Ok(views.html.index(produktDao.all()))
  }

  def env() = Action { implicit request: Request[AnyContent] =>
    Ok("Nothing to see here")
    //Ok(System.getenv("JDBC_DATABASE_URL"))
  }

  val produktForm = Form(
    mapping(
      "name" -> text(),
      "price" -> number())(Produkt.apply)(Produkt.unapply))

  def insertProdukt = Action.async { implicit request =>
    val produkt: Produkt = produktForm.bindFromRequest.get
    produktDao.insert(produkt).map(_ => Redirect(routes.HomeController.index))
  }

  val userForm = Form(
    mapping(
      "username" -> text(),
      "password" -> text())(User.apply)(User.unapply))
  def insertUser = Action.async { implicit request =>
    val user: User = userForm.bindFromRequest.get
    userDAO.insert(user).map(_ => Redirect(routes.HomeController.index))
    userDAO.all().map { case (users) =>
      var isValid = false
      users.map{ u =>
        if(user.username.equals(u.username)) isValid = true
      }
      if(isValid) NotFound
      else Redirect(routes.HomeController.index())
    }
  }

  def checkLogin = Action.async { implicit  request =>
    val user: User = userForm.bindFromRequest.get
    userDAO.all().map { case (users) =>
      var isValid = false
      users.map{ u =>
      if(user.username.equals(u.username) && user.password.equals(u.password)) isValid = true
      }
      if(isValid) Redirect(routes.HomeController.home)
      else NotFound
    }
  }

  def signup() = Action.async {
    userDAO.all().map { case (users) => Ok(views.html.signup()) }
    //Ok(views.html.index(produktDao.all()))
  }

  def add() = Action.async {
    produktDao.all().map { case (produkte) => Ok(views.html.add()) }
    //Ok(views.html.index(produktDao.all()))
  }
  def search() = Action.async {
    articleDAO.all().map { case (articles) => Ok(views.html.search(articles)) }
  }

  def contact() = Action.async {
    articleDAO.all().map { case (articles) => Ok(views.html.contact(articles)) }
  }

  val articleForm = Form(
    mapping (
      "name" -> text(),
      "price" -> number(),
      "description" -> text()
    )(Article.apply)(Article.unapply)
  )

  def insertArticle=Action.async{ implicit request =>
    val article : Article =articleForm.bindFromRequest.get
    articleDAO.insert(article).map(_ => Redirect(routes.HomeController.add))
  }
}