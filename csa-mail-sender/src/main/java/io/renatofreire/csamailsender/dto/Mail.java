package io.renatofreire.csamailsender.dto;

import java.util.HashMap;
import java.util.Map;

public class Mail {

    private String mailFrom;
    private String mailTo;
    private String subject;
    private String templateName;
    private Map<String, Object> properties;

    // Private constructor to be used by the builder
    private Mail(MailBuilder builder) {
        this.mailFrom = builder.mailFrom;
        this.mailTo = builder.mailTo;
        this.subject = builder.subject;
        this.templateName = builder.templateName;
        this.properties = builder.properties;
    }

    // Getter methods
    public String getMailFrom() {
        return mailFrom;
    }

    public String getMailTo() {
        return mailTo;
    }

    public String getSubject() {
        return subject;
    }

    public String getTemplateName() {
        return templateName;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    // Static Builder class
    public static class MailBuilder {

        private String mailFrom;
        private String mailTo;
        private String subject;
        private String templateName;
        private Map<String, Object> properties = new HashMap<>();

        // Builder methods for each field
        public MailBuilder mailFrom(String mailFrom) {
            this.mailFrom = mailFrom;
            return this;
        }

        public MailBuilder mailTo(String mailTo) {
            this.mailTo = mailTo;
            return this;
        }

        public MailBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public MailBuilder templateName(String templateName) {
            this.templateName = templateName;
            return this;
        }

        // Method to set properties map
        public MailBuilder properties(Map<String, Object> properties) {
            this.properties = properties;
            return this;
        }

        // Method to add a single property
        public MailBuilder property(String key, Object value) {
            this.properties.put(key, value);
            return this;
        }

        // Build method to create an instance of Mail
        public Mail build() {
            return new Mail(this);
        }
    }
}