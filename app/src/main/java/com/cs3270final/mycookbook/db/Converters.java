package com.cs3270final.mycookbook.db;

import androidx.room.TypeConverter;

import java.util.Date;

// Room databases can't handle date types, so this converts between dates and Long types which the
// db can handle.
public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

}
