package dev.upscairs.utils;

public abstract class StringConverter {

    public static Class<?> getFormattableDataType(String input) {

        if (input == null || input.isEmpty()) {
            return null;
        }
        else if(input.equals("true") || input.equals("false")) {
            return Boolean.class;
        }

        try {
            double doubleValue = Double.parseDouble(input);

            if(doubleValue == (int)doubleValue) {
                return Integer.class;
            }
            else {
                return Double.class;
            }

        } catch (NumberFormatException e) {
            return String.class;
        }

    }


}
