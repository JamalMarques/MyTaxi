package com.mytaxi.app.mvp.contract;

import com.mytaxi.app.models.Coordinate;
import com.mytaxi.app.models.Vehicle;
import com.mytaxi.app.mvp.contract.base.PresenterCoreInterface;
import com.mytaxi.app.mvp.contract.base.RetrofitManager;

import java.util.List;

public interface MapContract {

    interface View {
        void refreshVehiclesComponent(List<Vehicle> list);
    }

    interface Presenter extends PresenterCoreInterface {
    }

    interface Model extends RetrofitManager {
        void updatePoints(Coordinate pointOne, Coordinate pointTwo);

        /*Bus events*/
        class OnVehiclesInAreaSucess {

            private List<Vehicle> vehicles;

            public OnVehiclesInAreaSucess(List<Vehicle> vehicles) {
                this.vehicles = vehicles;
            }

            public List<Vehicle> getVehicles() {
                return vehicles;
            }
        }

        class OnVehiclesInAreaFail {
            private Throwable t;

            public OnVehiclesInAreaFail(Throwable t) {
                this.t = t;
            }

            public Throwable getT() {
                return t;
            }
        }
    }
}
