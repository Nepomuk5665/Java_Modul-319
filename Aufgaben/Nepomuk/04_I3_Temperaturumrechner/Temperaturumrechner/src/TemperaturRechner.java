
public class TemperaturRechner {
    /**
     * Rechnet Celsius in Fahrenheit um
     */
    public double celsiusNachFahrenheit(double celsius) {
        return (celsius * 9/5) + 32;
    }

    /**
     * Rechnet Fahrenheit in Celsius um
     */
    public double fahrenheitNachCelsius(double fahrenheit) {
        return (fahrenheit - 32) * 5/9;
    }
}