/**
 * Sensor que mide la temperatura del aire en grados Celsius.
 */
public class SensorTemperatura extends Sensor {

    private static final double LIMITE_BAJO = 18.0;
    private static final double LIMITE_ALTO = 30.0;

    public SensorTemperatura(String id, String ubicacion) {
        super(id, ubicacion, "°C");
    }

    /**
     * Devuelve la última medición expresada en grados Fahrenheit.
     */
    public double convertirAFahrenheit() {
        return getUltimaMedicion() * 9 / 5 + 32;
    }

    @Override
    public String evaluarMedicion() {
        double medicion = getUltimaMedicion();

        if (medicion < LIMITE_BAJO) {
            return "Temperatura baja";
        } else if (medicion > LIMITE_ALTO) {
            return "Temperatura alta";
        } else {
            return "Temperatura adecuada";
        }
    }

}