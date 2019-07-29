/*
 * Copyright 2017, OpenRemote Inc.
 *
 * See the CONTRIBUTORS.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.openremote.model.notification;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.openremote.model.value.ArrayValue;
import org.openremote.model.value.ObjectValue;
import org.openremote.model.value.Values;

import java.util.List;

import static org.openremote.model.util.TextUtil.isNullOrEmpty;

public class PushNotificationMessage extends AbstractNotificationMessage {

    public enum TargetType {
        DEVICE,
        TOPIC,
        CONDITION
    }

    public enum MessagePriority {
        NORMAL,
        HIGH
    }

    public static final String TYPE = "push";

    protected String title;
    protected String body;
    protected PushNotificationAction action;
    protected List<PushNotificationButton> buttons;
    protected ObjectValue data;
    protected MessagePriority priority;
    protected TargetType targetType;
    protected String target;
    protected Long ttlSeconds;

    @JsonCreator
    public PushNotificationMessage(@JsonProperty("title") String title,
                                   @JsonProperty("body") String body,
                                   @JsonProperty("action") PushNotificationAction action,
                                   @JsonProperty("buttons") List<PushNotificationButton> buttons,
                                   @JsonProperty("data") ObjectValue data,
                                   @JsonProperty("priority") MessagePriority priority,
                                   @JsonProperty("targetType") TargetType targetType,
                                   @JsonProperty("target") String target,
                                   @JsonProperty("expiration") Long ttlSeconds) {
        super(TYPE);
        this.title = title;
        this.body = body;
        this.action = action;
        this.buttons = buttons;
        this.priority = priority;
        this.targetType = targetType;
        this.target = target;
        this.data = data;
        this.ttlSeconds = ttlSeconds;
    }

    public PushNotificationMessage(String title,
                                   String body,
                                   PushNotificationAction action,
                                   List<PushNotificationButton> buttons,
                                   ObjectValue data) {
        super(TYPE);
        this.title = title;
        this.body = body;
        this.action = action;
        this.buttons = buttons;
        this.data = data;
    }

    public PushNotificationMessage() {
        super(TYPE);
    }

    public String getTitle() {
        return title;
    }

    public PushNotificationMessage setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getBody() {
        return body;
    }

    public PushNotificationMessage setBody(String body) {
        this.body = body;
        return this;
    }

    public PushNotificationAction getAction() {
        return action;
    }

    public PushNotificationMessage setAction(PushNotificationAction action) {
        this.action = action;
        return this;
    }

    public List<PushNotificationButton> getButtons() {
        return buttons;
    }

    public PushNotificationMessage setButtons(List<PushNotificationButton> buttons) {
        this.buttons = buttons;
        return this;
    }

    public ObjectValue getData() {
        return data;
    }

    public PushNotificationMessage setData(ObjectValue data) {
        this.data = data;
        return this;
    }

    public MessagePriority getPriority() {
        return priority;
    }

    public PushNotificationMessage setPriority(MessagePriority priority) {
        this.priority = priority;
        return this;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public PushNotificationMessage setTargetType(TargetType targetType) {
        this.targetType = targetType;
        return this;
    }

    public String getTarget() {
        return target;
    }

    public PushNotificationMessage setTarget(String target) {
        this.target = target;
        return this;
    }

    public Long getTtlSeconds() {
        return ttlSeconds;
    }

    public PushNotificationMessage setTtlSeconds(Long ttlSeconds) {
        this.ttlSeconds = ttlSeconds;
        return this;
    }

    @Override
    public ObjectValue toValue() {
        ObjectValue val = Values.createObject();
        if (!isNullOrEmpty(title)) {
            val.put("title", title);
        }
        if (!isNullOrEmpty(body)) {
            val.put("body", body);
        }
        if (action != null) {
            val.put("action", action.toValue());
        }
        if (buttons != null && !buttons.isEmpty()) {
            ArrayValue arrVal = Values.createArray();
            arrVal.addAll(buttons.stream().map(PushNotificationButton::toValue).toArray(ObjectValue[]::new));
            val.put("buttons", arrVal);
        }
        if (data != null) {
            val.put("data", data);
        }
        if (priority != null) {
            val.put("priority", Values.create(priority.name()));
        }
        if (targetType != null) {
            val.put("targetType", Values.create(targetType.name()));
        }
        if (!isNullOrEmpty(target)) {
            val.put("target", Values.create(target));
        }
        if (ttlSeconds != null) {
            val.put("ttlSeconds", Values.create(ttlSeconds));
        }
        return val;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
            "title='" + title + '\'' +
            ", body='" + body + '\'' +
            ", action=" + action +
            ", buttons=" + buttons +
            ", data=" + data +
            ", priority=" + priority +
            ", targetType=" + targetType +
            ", target='" + target + '\'' +
            ", ttlSeconds=" + ttlSeconds +
            '}';
    }
}
