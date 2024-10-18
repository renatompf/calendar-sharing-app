BEGIN;

CREATE TABLE shared_calendars (
                                  calendar_id UUID NOT NULL,
                                  user_id UUID NOT NULL,
                                  permission VARCHAR(20) NOT NULL,

                                  PRIMARY KEY (calendar_id, user_id),

                                  CONSTRAINT fk_calendar
                                      FOREIGN KEY (calendar_id)
                                          REFERENCES calendar (id) ON DELETE CASCADE,

                                  CONSTRAINT fk_user
                                      FOREIGN KEY (user_id)
                                          REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE shared_calendar_validation_token (
                                                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                                  token VARCHAR(255) NOT NULL,
                                                  created_at TIMESTAMP NOT NULL,
                                                  confirmed_at TIMESTAMP,
                                                  permissions VARCHAR(50) NOT NULL,
                                                  calendar_id UUID,
                                                  shared_with UUID,
                                                  CONSTRAINT fk_calendar FOREIGN KEY (calendar_id) REFERENCES calendar (id) ON DELETE CASCADE,
                                                  CONSTRAINT fk_shared_with FOREIGN KEY (shared_with) REFERENCES users (id) ON DELETE CASCADE
);

COMMIT;