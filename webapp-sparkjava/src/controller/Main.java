package controller;

import model.Car;
import model.SQLiteDatabase;
import org.json.JSONException;
import org.json.JSONObject;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.velocity.VelocityTemplateEngine;

import java.sql.SQLException;
import java.util.HashMap;

import static spark.Spark.*;

public class Main {

    private static SQLiteDatabase db = null;

    public static void main(String[] args) {
        db = new SQLiteDatabase();

        VelocityTemplateEngine engine = new VelocityTemplateEngine();
        staticFiles.location("/public");

        get("/", Main::pageHome, engine);
        get("/car/:id", Main::pageDetail, engine);
        get("/cars/new", Main::pageNew, engine);
        post("/cars", Main::createCar);
        get("/cars/edit/:id", Main::pageEdit, engine);
        post("/carsUp", Main::updateCar);
        get("/cars/remove/:id", Main::pageRemoveCar);
        get("/api/cars/:id", Main::pageAPI);
    }

    private static ModelAndView pageHome(Request req, Response res) {
        HashMap<String, Object> model = new HashMap<>();

        try {
            model.put("cars", db.getDao().queryForAll());
        } catch (SQLException ex){
            System.out.println(ex);
        }

        return new ModelAndView(model, "view/home.vm");
    }

    private static ModelAndView pageDetail(Request req, Response res) {
        HashMap<String, Object> model = new HashMap<>();

        Integer id = Integer.parseInt(req.params("id"));

        try {
            model.put("car", db.getDao().queryForId( id ));
        } catch (SQLException ex){
            System.out.println(ex);
        }

        return new ModelAndView(model, "view/detail.vm");
    }

    private static ModelAndView pageNew(Request req, Response res) {
        HashMap<String, Object> model = new HashMap<>();
        return new ModelAndView(model, "view/new.vm");
    }

    private static Object createCar(Request req, Response res) {
        Integer id = Integer.parseInt(req.queryParams("id"));
        System.out.println("*****************************");
        System.out.println("criando id:" + id);
        System.out.println("****comando");
        String brand = req.queryParams("brand");
        String model = req.queryParams("model");
        String color = req.queryParams("color");

        try {
            db.getDao().create( new Car(id, brand, model, color) );
        } catch (SQLException ex){
            System.out.println(ex);
            return "Internal server error!";
        }

        res.redirect("/");

        return "ok";
    }

    private static ModelAndView pageEdit(Request req, Response res) {
        Integer id = Integer.parseInt(req.params("id"));
        HashMap<String, Object> model = new HashMap<>();


        try {
            model.put("car", db.getDao().queryForId( id ));
        } catch (SQLException ex){
            System.out.println(ex);
        }

        return new ModelAndView(model, "view/edit.vm");
    }

    private static Object updateCar(Request req, Response res) {
        Integer id = Integer.parseInt(req.queryParams("id"));
        System.out.println("*****************************");
        System.out.println("editando id:" + id);
        System.out.println("****comando");
        String brand = req.queryParams("brand");
        String model = req.queryParams("model");
        String color = req.queryParams("color");


        try {
            db.getDao().update(new Car(id, brand, model, color));
        } catch (SQLException ex){
            System.out.println(ex);
            return "Internal server error!";
        }

        res.redirect("/");

        return "ok";
    }

    private static Object pageRemoveCar(Request req, Response res) {
        Integer id = Integer.parseInt(req.params("id"));
        System.out.println("*****************************");
        System.out.println("removendo id:" + id);
        System.out.println("****comando");
        try {
            db.getDao().deleteById(id);
        } catch (SQLException ex){
            System.out.println(ex);
            return "Internal server error!";
        }

        res.redirect("/");

        return "ok";
    }

    private static JSONObject pageAPI(Request req, Response res) {
        Integer id = Integer.parseInt(req.params("id"));

        System.out.println("*****************************");
        System.out.println("API id:" + id);
        System.out.println("****comando");
        JSONObject carroJson = new JSONObject();

        try {
            System.out.println("obj car " + db.getDao().queryForId(id).toString());
            Car carAux = db.getDao().queryForId(id);

            carroJson.put("id", carAux.getId());
            carroJson.put("Model", carAux.getModel());
            carroJson.put("Brand", carAux.getBrand());
            carroJson.put("Color", carAux.getColor());

            System.out.println("obj JSON" + carroJson.toString());

        } catch (JSONException ex){
            System.out.println(ex);
        } catch (SQLException ex){
            System.out.println(ex);
        }
        res.redirect("/");

        res.header("Content-Type", "application/json");

        return carroJson;
    }
}
