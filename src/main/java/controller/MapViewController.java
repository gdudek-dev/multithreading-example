package controller;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import model.display.CarDisplayManager;
import model.drivers.CarDriver;
import model.drivers.TrainDriver;
import model.spawner.CarSpawner;
import model.spawner.TrainSpawner;
import model.vechicle.Car;
import model.vechicle.Train;
import utils.AddVehicleImageToCorrectPane;


public class MapViewController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private AnchorPane bottomPane;
    @FXML
    private AnchorPane topPane;



    @FXML
    public void initialize() {
        startSpawning();
    }


    private void startSpawning()
    {
        CarSpawner carSpawner = new CarSpawner();
        TrainSpawner trainSpawner = new TrainSpawner();

        Runnable cars = () -> spawnCars(carSpawner);
        Thread car = new Thread(cars);
        car.start();

        Runnable trains = () -> spawnTrain(trainSpawner);
        Thread train = new Thread(trains);
        train.start();
    }

    private void spawnCars(CarSpawner carSpawner)
    {
        while(true)
        {
            Runnable r = () ->
            {
                Car car = carSpawner.spawnCar();
                if(car!=null)
                {
                    CarDisplayManager carDisplayManager = new CarDisplayManager(bottomPane,topPane);
                    CarDriver carDriver = new CarDriver(car,carDisplayManager);
                    carSpawner.increaseAmountOfSpawnedCars();
                    AddVehicleImageToCorrectPane.addVehicleImageToCorrectPane(car,bottomPane,topPane);
                    carDriver.drive();
                    carSpawner.decreaseAmountOfSpawnedCars();
                }
            };
            Thread t = new Thread(r);
            t.start();
            try
            {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void spawnTrain(TrainSpawner trainSpawner)
    {
        while(true) {
            Train train = trainSpawner.spawnTrain();
            TrainDriver trainDriver = new TrainDriver(train);
            Platform.runLater(()->topPane.getChildren().add(train.getImageView()));
            trainDriver.drive();
            try
            {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}
