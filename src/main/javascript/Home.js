import React from "react";
import { useLocation } from "react-router-dom";
import NavBar from "./NavBar";
import HomeTimer from "./HomeTimer";

const Home = () => {
  const location = useLocation();

  const calculateRemainingSecondsforDay = () => {
    const now = new Date();
    const endOfDay = new Date(now);
    endOfDay.setHours(23, 59, 59, 999);
    return Math.floor((endOfDay - now) / 1000);
  };

  const calculateRemainingSecondsforHour = () => {
    const now = new Date();
    const endOfHour = new Date(now);
    endOfHour.setMinutes(59, 59, 999);
    return Math.floor((endOfHour - now) / 1000);
  };

  const calculateRemainingSecondsforLife = () => {
    const currentAge = 18;
    const retirementAge = 60;
    return (retirementAge - currentAge) * 365 * 24 * 60 * 60;
  };

  const calculateRemainingSecondsforMonth = () => {
    const now = new Date();
    const endOfMonth = new Date(
      now.getFullYear(),
      now.getMonth() + 1,
      0,
      23,
      59,
      59,
      999
    );
    return Math.floor((endOfMonth - now) / 1000);
  };

  const calculateRemainingSecondsforYear = () => {
    const now = new Date();
    const endOfYear = new Date(now.getFullYear() + 1, 0, 0, 23, 59, 59, 999);
    return Math.floor((endOfYear - now) / 1000);
  };
  

  return (
    <div
      style={{
        width: "100%",
        height: "100vh",
        overflow: "hidden",
      }}
    >
      <NavBar />
      <div className="text-center">
        <div
          style={{
            width: "100%", // Adjusted width to cover the entire container
            height: "100%", // Adjusted height to cover the entire container
            objectFit: "cover",
            position: "absolute",
            top: 0,
            left: 0,
            zIndex: -1,
            background: "linear-gradient(to bottom right, #FFD9FB, white)", // White fade to the right
          }}
        >
          <h1
            style={{
              position: "absolute",
              top: "10%",
              left: "50%",
              transform: "translate(-50%, -50%)",
              textAlign: "center",
              zIndex: 1,
              background: "linear-gradient(to right,#fd18fe,purple)",
              WebkitBackgroundClip: "text",
              color: "transparent",
            }}
          >
            Welcome to NextTimer
          </h1>
          <div
            style={{
              position: "absolute",
              top: "50%",
              left: "50%",
              transform: "translate(-50%, -50%)",
              textAlign: "center",
              zIndex: 1,
              color: "#fd18fe",
            }}
          >
            <div>
              <HomeTimer seconds={calculateRemainingSecondsforLife()} />{" "}Remaining Till Next Life
            </div>
            <div>
              <HomeTimer seconds={calculateRemainingSecondsforYear()} />{" "}Remaining Till Next Year
            </div>
            <div>
              <HomeTimer seconds={calculateRemainingSecondsforMonth()} />{" "}Remaining Till Next Month
            </div>
            <div>
              <HomeTimer seconds={calculateRemainingSecondsforDay()} />{" "}Remaining Till Next Day
            </div>
            <div>
              <HomeTimer seconds={calculateRemainingSecondsforHour()} />{" "}Remaining Till Next Hour
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Home;
