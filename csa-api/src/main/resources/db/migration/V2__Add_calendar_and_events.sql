BEGIN;

CREATE TABLE calendar
(
    id      UUID         NOT NULL,
    name    VARCHAR(255) NOT NULL,
    user_id UUID,
    CONSTRAINT pk_calendar PRIMARY KEY (id)
);

CREATE TABLE event
(
    id          UUID                        NOT NULL,
    title       VARCHAR(255)                NOT NULL,
    description VARCHAR(255),
    start_time  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_time    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    calendar_id UUID,
    CONSTRAINT pk_event PRIMARY KEY (id)
);

ALTER TABLE calendar
    ADD CONSTRAINT FK_CALENDAR_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE event
    ADD CONSTRAINT FK_EVENT_ON_CALENDAR FOREIGN KEY (calendar_id) REFERENCES calendar (id);

COMMIT;