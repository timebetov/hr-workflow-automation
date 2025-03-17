package com.hrworkflow.usersservice.dto.workflow;

public enum ApplicationStatus {

    PENDING,    // Кандидат отправил заявку, но её ещё не рассмотрели
    REJECTED,   // Заявка отклонена (не подходит, не прошёл резюме-скрининг)
    HIRED,      // Кандидат успешно прошёл процесс найма и получил оффер
    INVITED,    // Заявка одобрена, кандидат приглашён на интервью
    DECLINED,   // Кандидат отказался от оффера
    WITHDRAWN   // Кандидат сам отозвал заявку

}
