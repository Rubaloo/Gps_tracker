package com.example.ruben.gps_tracker;

public class GTSmsFormat {

    public enum Location
    {
        Code,
        Credit0, Credit1, Credit2,
        BatteryStatus0, BatteryStatus1,
        StatusData0, StatusData1,
        Speed0, Speed1, Speed2,
        Time0, Time1, Time2, Time3, Time4, Time5, Time6, Time7, Time8, Time9, Time10, Time11,
        Location100, Location101, Location102, Location103, Location104, Location105, Location106, Location107, Location108, Location109,
        Location110, Location111, Location112, Location113, Location114, Location115, Location116, Location117, Location118, Location119,
        Location200, Location201, Location202, Location203, Location204, Location205, Location206, Location207, Location208, Location209,
        Location210, Location211, Location212, Location213, Location214, Location215, Location216, Location217, Location218, Location219,
        Location300, Location301, Location302, Location303, Location304, Location305, Location306, Location307, Location308, Location309,
        Location310, Location311, Location312, Location313, Location314, Location315, Location316, Location317, Location318, Location319,
        Location400, Location401, Location402, Location403, Location404, Location405, Location406, Location407, Location408, Location409,
        Location410, Location411, Location412, Location413, Location414, Location415, Location416, Location417, Location418, Location419,
        Size
    };

    public enum Settings
    {
        Code,
        SoundAlarm,
        VisualAlarm,
        AutoTrack,
        UserTelephone0, UserTelephone1, UserTelephone2, UserTelephone3, UserTelephone4, UserTelephone5,
        UserTelephone6, UserTelephone7, UserTelephone8, UserTelephone9, UserTelephone10, UserTelephone11,
        Time0, Time1, Time2, Time3, Time4, Time5, Time6, Time7, Time8, Time9,
        AlarmClock0, AlarmClock1, AlarmClock2, AlarmClock3, AlarmClock4, AlarmClock5, AlarmClock6,
        AlarmClock7, AlarmClock8, AlarmClock9,
        Reserved0, Reserved1, Reserved2, Reserved3, Reserved4, Reserved5, Reserved6, Reserved7, Reserved8, Reserved9,
        Reserved10, Reserved11, Reserved12, Reserved13, Reserved14, Reserved15, Reserved16, Reserved17, Reserved18, Reserved19,
        Reserved20, Reserved21, Reserved22, Reserved23, Reserved24, Reserved25, Reserved26, Reserved27, Reserved28, Reserved29,
        Reserved30, Reserved31, Reserved32, Reserved33, Reserved34, Reserved35, Reserved36, Reserved37, Reserved38, Reserved39,
        Reserved40, Reserved41, Reserved42, Reserved43, Reserved44, Reserved45, Reserved46, Reserved47, Reserved48, Reserved49,
        Reserved50, Reserved51, Reserved52, Reserved53, Reserved54, Reserved55, Reserved56, Reserved57, Reserved58, Reserved59,
        Reserved60, Reserved61, Reserved62, Reserved63, Reserved64,
        Size
    };

    static int getIndex(Enum format)
    {
        return format.ordinal();
    }
}
