package com.kuoni.qa.automation.helper

import com.kuoni.qa.automation.util.TestConf
import com.typesafe.config.Config
import com.typesafe.config.ConfigException
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

import static com.kuoni.qa.automation.util.TestConf.*

/**
 * Created by Joseph Sebastian on 12/10/2015.
 */
abstract class BaseData {

    protected Config conf = null
    protected DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MMM-yy");
    protected Config defaultConf
    protected String subPath = ""
    protected static final String defaultPath = "default"

    protected abstract void init()

    BaseData(String path) {
        conf = null == conf ? loadConfig(path) : conf
        init()
    }

    BaseData(String path, String subPath) {
        this.subPath = subPath
        conf = loadConfig(path)
        init()
    }

    BaseData(String subPath, Config conf) {
        this.subPath = subPath
        this.conf = conf
        init()
    }

    protected Config loadConfig(String path) {
        getConfig(path)
    }

    protected String getConfString(path) {
        String val
        try {
            val = conf.getString(path)
        } catch (Exception e) {
            val = ""
        }
        return val
    }

    protected List<String> getStringList(path) {
        List<String> val
        try {
            val = conf.getStringList(path)
        } catch (Exception e) {
            val = Collections.emptyList()
        }
        return val
    }

    protected String getValOrDefault(path) {
        try {
            return conf.getString(path)
        } catch (ConfigException e) {
            return getDefault(path)
        }
    }

    protected Boolean getValueOrNull(path) {
        try {
            return conf.getBoolean(path)
        } catch (ConfigException e) {
            return null
        }
    }

    protected List<Integer> getIntegerList(path) {
        List<Integer> val
        try {
            val = conf.getIntList(path)
        } catch (Exception e) {
            val = Collections.emptyList()
        }
        return val
    }

    protected int getDaysToAdd(String path) {
        String dateStr = getConfString(path)
        if (dateStr.contains("-")) {
            DateTime dateTime = formatter.parseDateTime(dateStr)
            return Days.daysBetween(new LocalDate(), new LocalDate(dateTime)).days
        } else
            return dateStr.toInteger()
    }

    protected String getStringOrDefault(String path) {
        String val = getConfString(path)
        try {
            if (defaultConf != null && val.empty) val = defaultConf.getAnyRef(path).toString()
        } catch (Exception e) {
            val = ""
        }

        return val
    }

    protected String getSubPath() {
        subPath
    }

    protected Config getDefaultConfig() {
        conf
    }

    public String getDefault(String path) {
        getDefaultConfig().getString(path)
    }

}
