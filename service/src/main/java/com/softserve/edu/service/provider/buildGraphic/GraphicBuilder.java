package com.softserve.edu.service.provider.buildGraphic;

import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.user.User;

import java.text.ParseException;
import java.util.*;

public class GraphicBuilder {
    private static StringBuilder strBuild;

    public static List<MonthOfYear> listOfMonths(Date dateFrom, Date dateTo) throws ParseException {
        Calendar start = Calendar.getInstance();
        start.setTime(dateFrom);
        rollDateToFirstDayOfMonth(start);

        Calendar end = Calendar.getInstance();
        end.setTime(dateTo);
        rollDateToFirstDayOfMonth(end);

        List<MonthOfYear> months = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Date date = start.getTime();
        while(start.before(end) || start.equals(end)) {
            calendar.setTime(date);
            MonthOfYear item = new MonthOfYear(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
            months.add(item);
            start.add(Calendar.MONTH, 1);
            date = start.getTime();
        }
        return months;
    }

    public static List<ProviderEmployeeGraphic> builderData(List<Verification> verificationList,
                                                           List<MonthOfYear> months, List<User> listOfEmployee) throws ParseException {
        Map<String, ProviderEmployeeGraphic> employeeGraphicMap = new HashMap<>();

        for (Verification verification : verificationList) {
            Calendar expirDate = Calendar.getInstance();
            ProviderEmployeeGraphic graphicItem;

            if (employeeGraphicMap.containsKey(verification.getProviderEmployee().getUsername())) {
                graphicItem = employeeGraphicMap.get(verification.getProviderEmployee().getUsername());
            } else {
                graphicItem = new ProviderEmployeeGraphic();
                graphicItem.monthList = months;
                graphicItem.data = new double[months.size()];
                graphicItem.name = verification.getProviderEmployee().getUsername();
                employeeGraphicMap.put(verification.getProviderEmployee().getUsername(), graphicItem);
            }
            expirDate.setTime(verification.getSentToCalibratorDate());
            MonthOfYear item = new MonthOfYear(expirDate.get(Calendar.MONTH), expirDate.get(Calendar.YEAR));
            int indexOfMonth = months.indexOf(item);
            graphicItem.data[indexOfMonth]++;
        }
        List<ProviderEmployeeGraphic> graphicItemsList = listOfProviderEmployeeGrafic(employeeGraphicMap, listOfEmployee);

        return graphicItemsList;
    }


    public static List<ProviderEmployeeGraphic> listOfProviderEmployeeGrafic(Map<String,
            ProviderEmployeeGraphic> employeeGraphicMap, List<User> listOfEmployee) {
        List<ProviderEmployeeGraphic> graphicItemsList = new ArrayList<>();
        for (Map.Entry<String, ProviderEmployeeGraphic> item : employeeGraphicMap.entrySet()) {
            graphicItemsList.add(item.getValue());
        }
        for (ProviderEmployeeGraphic provEmp : graphicItemsList) {
            for (User user : listOfEmployee) {
                if (provEmp.name.equals(user.getUsername())) {
                    strBuild = new StringBuilder();
                    strBuild.append(user.getLastName()).append(" ");
                    strBuild.append(user.getFirstName()).append(" ");
                    strBuild.append(user.getMiddleName());
                    provEmp.name = strBuild.toString();
                }
            }
        }
        return graphicItemsList;
    }


    private static void rollDateToFirstDayOfMonth(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}