package sample;

public class Constant {

    public static final String[] types = {"Fly", "Fire", "Water", "Electric", "Ice"};
    public static final String[] COLORS = {"Black", "Blue", "Orange", "Red", "Green"};

    public static boolean isAllInputsValid(String name, String type, String color){
        if(name.equals("") || type == null || color == null){
            return false;
        }
        return true;
    }

    public static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    public static String pickColor(String color) {
        String colorStr = "-fx-text-fill:";
        colorStr = colorStr + color.toLowerCase() + ";";
        return colorStr;
    }
}
