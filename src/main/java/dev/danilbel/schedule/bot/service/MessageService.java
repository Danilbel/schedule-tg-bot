package dev.danilbel.schedule.bot.service;

import dev.danilbel.schedule.domain.DayOfWeek;
import dev.danilbel.schedule.domain.Schedule;
import dev.danilbel.schedule.domain.ScheduleDateTime;
import dev.danilbel.schedule.domain.ScheduleDay;
import dev.danilbel.schedule.domain.TimeTable;
import dev.danilbel.schedule.domain.WeekName;
import dev.danilbel.schedule.domain.service.ScheduleDayService;
import dev.danilbel.schedule.domain.service.ScheduleWeekService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Service
public class MessageService {

    ScheduleWeekService scheduleWeekService;
    ScheduleDayService scheduleDayService;

    public String getStartMessage() {

        return """
                <b>Хеййоу!
                Я бот, який допоможе з розкладом пар КПІ!</b>

                Які пари на поточний та наступний тиждень, на сьогодні та наступний робочий день. Яка пара зараз та яка наступна. Скільки часу залишилось до кінця пари або перерви. Це все я дізнаюсь з сайту <a href="https://schedule.kpi.ua/">розкладу КПІ</a> та надам тобі відповідь.

                – Користуйся мною у особистих повідомленнях або додай до чату групи.
                – Дізнайся про всі команди бота: /help

                <i>v1.0.0 (beta)
                Бот працює за умови роботи API сайту https://schedule.kpi.ua/api
                Автор боту: @danillbel</i>
                """;
    }

    public String makeCurrentPairMessageByScheduleDateTime(Schedule schedule, ScheduleDateTime scheduleDateTime) {

        var day = scheduleWeekService.getSortedScheduleDayOrNullByDayOfWeek(
                schedule,
                scheduleDateTime.getWeekName(),
                scheduleDateTime.getDayOfWeek()
        );

        var check = checkDayOfWeekAndPairs(scheduleDateTime.getDayOfWeek(), day);

        if (check != null) return check;

        var timePairs = scheduleDayService.getSortedTimePairs(day);

        for (var timePair : timePairs) {

            if (scheduleDateTime.getTime().isBefore(timePair.getLocalTimeOfStartTime())) {

                var msg = "";

                if (scheduleDateTime.getTime().isBefore(TimeTable.FIRST_PAIR.getLocalTimeOfStartTime())) {

                    msg = "<b>Пари ще не почалися!</b>\n\n";
                } else {

                    msg = "<b>Зараз перерва!</b>\n\n";
                }

                var remainingTime = getRemainingTime(scheduleDateTime, timePair);

                msg += String.format("Наступна пара через <i>%d год. %d хв.</i>\n",
                        remainingTime.getHour(), remainingTime.getMinute());
                msg += scheduleDayService.pairsByTimeToString(day, timePair);

                return msg;

            } else if (scheduleDateTime.getTime().isBefore(timePair.getLocalTimeOfEndTime())) {

                var msg = "<b>Зараз пара:</b>\n\n";
                msg += scheduleDayService.pairsByTimeToString(day, timePair);

                return msg;
            }
        }

        return "<i>Пари вже закінчилися!</i>";
    }

    private String checkDayOfWeekAndPairs(DayOfWeek dayOfWeek, ScheduleDay scheduleDay) {

        if (dayOfWeek == DayOfWeek.SUNDAY) {

            return "<i>У неділю пар немає!</i>";
        }

        if (scheduleDay == null || scheduleDay.getPairs().isEmpty()) {

            return "<i>Сьогодні пар немає!</i>";
        }

        return null;
    }

    private LocalTime getRemainingTime(ScheduleDateTime scheduleDateTime, TimeTable timePair) {

        return timePair.getLocalTimeOfStartTime()
                .minusHours(scheduleDateTime.getTime().getHour())
                .minusMinutes(scheduleDateTime.getTime().getMinute());
    }

    public String makeNextPairMessageByScheduleDateTime(Schedule schedule, ScheduleDateTime scheduleDateTime) {

        var day = scheduleWeekService.getSortedScheduleDayOrNullByDayOfWeek(
                schedule,
                scheduleDateTime.getWeekName(),
                scheduleDateTime.getDayOfWeek()
        );

        var check = checkDayOfWeekAndPairs(scheduleDateTime.getDayOfWeek(), day);

        if (check != null) return check;

        var timePairs = scheduleDayService.getSortedTimePairs(day);

        for (var timePair : timePairs) {

            if (scheduleDateTime.getTime().isBefore(timePair.getLocalTimeOfStartTime())) {

                var remainingTime = getRemainingTime(scheduleDateTime, timePair);

                var msg = String.format("Наступна пара через <i>%d год. %d хв.</i>\n",
                        remainingTime.getHour(), remainingTime.getMinute());
                msg += scheduleDayService.pairsByTimeToString(day, timePair);

                return msg;
            }
        }

        return "<i>Пар більше немає!</i>";
    }

    public String makeRemainingTimeMessageByScheduleDateTime(Schedule schedule, ScheduleDateTime scheduleDateTime) {

        var day = scheduleWeekService.getSortedScheduleDayOrNullByDayOfWeek(
                schedule,
                scheduleDateTime.getWeekName(),
                scheduleDateTime.getDayOfWeek()
        );

        var check = checkDayOfWeekAndPairs(scheduleDateTime.getDayOfWeek(), day);

        if (check != null) return check;

        var timePairs = scheduleDayService.getSortedTimePairs(day);

        for (var timePair : timePairs) {

            if (scheduleDateTime.getTime().isBefore(timePair.getLocalTimeOfStartTime())) {

                var msg = scheduleDateTime.getTime().isBefore(TimeTable.FIRST_PAIR.getLocalTimeOfStartTime())
                        ? "<b>Пари ще не почалися, до наступної пари залишилося:</b>\n\n"
                        : "<b>До кінця перерви залишилося:</b>\n\n";

                var remainingTime = getRemainingTime(scheduleDateTime, timePair);

                msg += String.format("<i>%d год. %d хв. %d сек.</i>",
                        remainingTime.getHour(), remainingTime.getMinute(), remainingTime.getSecond());

                return msg;
            } else if (scheduleDateTime.getTime().isBefore(timePair.getLocalTimeOfEndTime())) {

                var remainingTime = getRemainingTime(scheduleDateTime, timePair);

                return String.format("<b>До кінця пари залишилося:</b>\n\n<i>%d год. %d хв. %d сек.</i>",
                        remainingTime.getHour(), remainingTime.getMinute(), remainingTime.getSecond());
            }
        }

        return "<i>Пари вже закінчилися!</i>";
    }

    public String getTimetableMessage() {

        StringBuilder msg = new StringBuilder("<b>Розклад пар:</b>\n\n");

        for (var time : TimeTable.values()) {
            msg.append(time).append("\n");
        }

        return msg.toString();
    }

    public String makeScheduleDayMessageByScheduleDateTime(Schedule schedule, ScheduleDateTime scheduleDateTime) {

        if (scheduleDateTime.getDayOfWeek() == DayOfWeek.SUNDAY) {

            return "<i>У неділю пар немає!</i>";
        }

        var scheduleDay = scheduleWeekService.getSortedScheduleDayOrNullByDayOfWeek(
                schedule,
                scheduleDateTime.getWeekName(),
                scheduleDateTime.getDayOfWeek()
        );

        var msg = String.format("<b>Пари на %s (%02d.%02d):</b>\n\n",
                scheduleDateTime.getDayOfWeek().getFullNameDay().toLowerCase(),
                scheduleDateTime.getDate().getDayOfMonth(),
                scheduleDateTime.getDate().getMonthValue());

        if (scheduleDay != null) {
            msg += scheduleDayService.scheduleDayToString(scheduleDay);
        } else {
            msg += "<i>На цей день пар немає!</i>";
        }

        return msg;
    }

    public String makeScheduleWeekMessageByWeekName(Schedule schedule, WeekName weekName) {

        var msg = String.format("<b>Пари на %s тиждень:</b>\n\n",
                weekName.getNameWeek().toLowerCase());

        msg += scheduleWeekService.scheduleWeekByWeekNameToString(schedule, weekName);

        return msg;
    }

    public String getHelpMessage() {

        return """
                <b>Команди бота:</b>
                                
                /start – почати роботу з ботом
                                
                /current – поточна пара
                /next – наступна пара
                                
                /last – скільки часу залишилось до кінця пари або перерви
                /timetable – розклад пар
                                
                /today – пари на сьогодні
                /next_day – пари на наступний робочий день
                                
                /current_week – пари на поточний тиждень
                /next_week – пари на наступний тиждень
                                
                /help – повний список команд
                """;
    }
}
