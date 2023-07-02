package dev.danilbel.schedule.domain.service;

import dev.danilbel.schedule.domain.SchedulePair;
import org.springframework.stereotype.Service;

@Service
public class SchedulePairService {

    public String schedulePairToString(SchedulePair pair) {

        var str = "– " + pair.getName() + "<i>";

        if (pair.getType() != null && !pair.getType().trim().isEmpty()) {
            str += ", " + pair.getType();
        }

        if (pair.getPlace() != null && !pair.getPlace().trim().isEmpty()) {
            str += ", place: " + pair.getPlace();
        }

        if (pair.getTeacherName() != null && !pair.getTeacherName().trim().isEmpty()) {
            str += ", " + pair.getTeacherName();
        }

        return str + "</i>";
    }
}
