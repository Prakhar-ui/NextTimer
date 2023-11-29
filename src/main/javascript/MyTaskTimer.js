import React, { useState, useEffect } from "react";
import { Row, Col, Button } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPlay, faPause, faSync } from "@fortawesome/free-solid-svg-icons";

const MyTaskTimer = ({ seconds }) => {
  const [hours, setHours] = useState(Math.floor(seconds / 3600));
  const [minutes, setMinutes] = useState(Math.floor((seconds % 3600) / 60));
  const [currentSeconds, setSeconds] = useState(seconds % 60);
  const [isRunning, setIsRunning] = useState(false);

  useEffect(() => {
    let timer;

    if (isRunning) {
      timer = setInterval(() => {
        if (hours === 0 && minutes === 0 && currentSeconds === 0) {
          alert("Timer Finished");
          clearInterval(timer);
          setIsRunning(false);
        } else {
          if (minutes === 0 && currentSeconds === 0) {
            setHours((prevHours) => prevHours - 1);
            setMinutes(59);
            setSeconds(59);
          } else if (currentSeconds === 0) {
            setMinutes((prevMinutes) => prevMinutes - 1);
            setSeconds(59);
          } else {
            setSeconds((prevSeconds) => prevSeconds - 1);
          }
        }
      }, 1000);
    }

    return () => clearInterval(timer);
  }, [isRunning, currentSeconds, minutes, hours]);

  const startTimer = () => {
    setIsRunning(true);
  };

  const pauseTimer = () => {
    setIsRunning(false);
  };

  const resetTimer = () => {
    setIsRunning(false);
    setHours(Math.floor(seconds / 3600));
    setMinutes(Math.floor((seconds % 3600) / 60));
    setSeconds(seconds % 60);
  };

  const formatTime = (value) => String(value).padStart(2, "0");

  return (
    <Row className="text-center mx-0 d-flex align-items-stretch">
      <Col >
        <Button variant="primary" onClick={isRunning ? pauseTimer : startTimer}>
          <FontAwesomeIcon icon={isRunning ? faPause : faPlay} />{" "}
          {isRunning ? "Pause" : "Start"}
        </Button>
      </Col>
      <Col >
        <Button variant="secondary" onClick={resetTimer}>
          <FontAwesomeIcon icon={faSync} /> Reset
        </Button>
      </Col>
      <Col  style={{ fontSize: "24px", fontWeight: "bold", color: "#3498db" }}>
        {formatTime(hours)}:{formatTime(minutes)}:{formatTime(currentSeconds)}
      </Col>
    </Row>
  );
};

export default MyTaskTimer;
