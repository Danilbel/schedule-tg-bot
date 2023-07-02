package dev.danilbel.schedule.bot.service;

import org.springframework.stereotype.Service;

@Service
public class MessageService {

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
