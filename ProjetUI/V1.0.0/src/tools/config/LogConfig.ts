// -----------------------------------------------------------------------------------
// https://github.com/vauxite-org/typescript-logging/blob/HEAD/log4ts-style/README.MD
// -----------------------------------------------------------------------------------
/*
A logger can log on different LogLevels:

    Trace
    Debug
    Info
    Warn
    Error
    Fatal
    Off



*/
/*--- config/LogConfig.ts ---*/
import {LogLevel} from "typescript-logging";
import {Log4TSProvider} from "typescript-logging-log4ts-style";

export const log4TSProvider = Log4TSProvider.createProvider("AwesomeLog4TSProvider", {
  level: LogLevel.Debug,
  groups: [{
    expression: new RegExp(".+"),
  }],
  dateFormatter: formatDate,
  argumentFormatter: formatArgument,
  channel: {
    type: "LogChannel",
    write: logMessage => console.log(`${logMessage.message}`),
  },
});
// 2023-08-05 13:48:37,660 DEBUG [cMyNavigationAngleConversion] computeAngleconversion: elem {} [{}]

function formatArgument(arg: unknown): string {
    if (arg === undefined) {
      return "undefined";
    }
    return ">>" + JSON.stringify(arg) + "<<";
  }

function formatDate(millisSinceEpoch: number): string {
    const date = new Date(millisSinceEpoch);
    const year = date.getFullYear();
    const month = padStart((date.getMonth() + 1).toString(), 2, "0");
    const day = padStart(date.getDate().toString(), 2, "0");
    const hours = padStart(date.getHours().toString(), 2, "0");
    const minutes = padStart(date.getMinutes().toString(), 2, "0");
    const seconds = padStart(date.getSeconds().toString(), 2, "0");
    const millis = padStart(date.getMilliseconds().toString(), 2, "0");
    return `[${year}-${month}-${day} ${hours}:${minutes}:${seconds},${millis}]`;
  }

   function padStart(value: string, length: number, fillChar: string = " ") {
    return padInternal(value, length, "start", fillChar);
  }
  
  function padInternal(value: string, length: number, padType: "start" | "end", fillChar: string = " ") {
    if (length <= value.length) {
      return value;
    }
    if (fillChar.length > 1) {
      throw new Error(`Fill char must be one char exactly, it is: ${fillChar.length}`);
    }
  
    const charsNeeded = length - value.length;
    let padding = "";
  
    for (let i = 0; i < charsNeeded; i++) {
      padding += fillChar;
    }
  
    if (padType === "start") {
      return padding + value;
    }
    return value + padding;
  }
