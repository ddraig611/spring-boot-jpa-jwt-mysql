import React from "react";
import {
    DatePicker,
    DayOfWeek
} from "office-ui-fabric-react";

/**
 * Date Picker Strings
 */
const datePickerStrings = {
    months: [
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    ],

    shortMonths: [
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "Jun",
        "Jul",
        "Aug",
        "Sep",
        "Oct",
        "Nov",
        "Dec"
    ],

    days: [
        "Sunday",
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday"
    ],

    shortDays: [
        "S",
        "M",
        "T",
        "W",
        "T",
        "F",
        "S"
    ],

    goToToday: "Go to Today"
};

/**
 * Date Picker Demo
 */
const Canvans = () => {
    // Render the component
    return (
        <div style={{width: "250px"}}>
            <DatePicker
                firstDayOfWeek={DayOfWeek.Sunday}
                placeholder="Select a date..."
                strings={datePickerStrings}
            />
        </div>
    );
};

export default Canvans;