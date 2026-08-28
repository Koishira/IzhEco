package com.example.izheco.utils;

public class PhoneFormatter {

    /**
     * Форматирует телефонный номер в три формата:
     * 1. 11 цифр: +7 (XXX) XXX-XX-XX
     * 2. 7 цифр: XXX-XX-XX
     * 3. 6 цифр: XXX-XXX
     *
     * @param phone Исходный номер телефона
     * @return Отформатированный номер
     */
    public static String formatPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return "";
        }

        // Удаляем все нецифровые символы
        String digits = phone.replaceAll("[^0-9]", "");

        // Проверяем длину и форматируем
        if (digits.length() == 11 && digits.startsWith("7")) {
            // Формат: +7 (XXX) XXX-XX-XX
            return String.format("+7 (%s) %s-%s-%s",
                    digits.substring(1, 4),    // код региона (912)
                    digits.substring(4, 7),    // первая тройка (345)
                    digits.substring(7, 9),    // первые две (67)
                    digits.substring(9, 11));  // последние две (89)
        }
        else if (digits.length() == 7) {
            // Формат: XXX-XX-XX
            return String.format("%s-%s-%s",
                    digits.substring(0, 3),    // первая тройка
                    digits.substring(3, 5),    // первые две
                    digits.substring(5, 7));   // последние две
        }
        else if (digits.length() == 6) {
            // Формат: XXX-XXX
            return String.format("%s-%s",
                    digits.substring(0, 3),    // первая тройка
                    digits.substring(3, 6));   // вторая тройка
        }

        // Если номер не подходит под форматы, возвращаем как есть
        return phone.trim();
    }
}