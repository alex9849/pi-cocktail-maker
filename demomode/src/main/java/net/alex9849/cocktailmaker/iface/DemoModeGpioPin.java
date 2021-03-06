package net.alex9849.cocktailmaker.iface;

import com.pi4j.io.gpio.Pin;

public class DemoModeGpioPin implements IGpioPin {
    private Pin pin;
    private boolean isHigh;

    DemoModeGpioPin(Pin pin) {
        this.pin = pin;
    }

    @Override
    public boolean isHigh() {
        return this.isHigh;
    }

    @Override
    public void setHigh() {
        this.isHigh = true;
        System.out.println("Pin " + pin.getAddress() + " stopped!");
    }

    @Override
    public void setLow() {
        this.isHigh = false;
        System.out.println("Pin " + pin.getAddress() + " started!");
    }
}
