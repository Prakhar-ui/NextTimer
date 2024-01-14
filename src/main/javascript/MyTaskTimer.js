import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Row, Col, Button } from "react-bootstrap";
import styled from "styled-components";
import NavBar from "./NavBar";
import { FaPlay, FaPause, FaStop } from "react-icons/fa";
import { config } from "process";
import axios from "axios";

const BlackSquare = styled.div`
  background-color: #000;
  margin: 10px;
  display: inline-block;
  border-radius: 5px;
  position: relative; /* Make the parent relative to position the pseudo-element */
`;

const BlackSquareDesc = styled.div`
  background-color: #000; /* Adjust padding for spacing between the number and the black square */
  margin: 10px;
  display: inline-block;
  border-radius: 5px; /* Adjust border radius for rounded corners */
`;

const StyledColDesc = styled.div`
  font-size: 50px;
  font-weight: bold;
  color: #fff; /* White text color */
  display: inline-block; /* Make sure each column is inline-block */
`;

const StyledCol = styled.div`
  font-size: 400px;
  font-weight: bold;
  color: #fff; /* White text color */
  display: inline-block; /* Make sure each column is inline-block */
`;

const StyledColon = styled.div`
  font-size: 300px;
  font-weight: bold;
  color: #fff; /* White text color */
  display: inline-block; /* Make sure each column is inline-block */
`;

const CenteredContainer = styled.div`
  width: 100%;
  height: 100%;
  overflow: hidden;
  padding-left: 0;
`;

const TimerContainer = styled.div`
  width: 100%;
  height: 100%;
  object-fit: cover;
  position: absolute;
  top: 0;
  left: 0;
  z-index: -1;
  text-align: center;
  background-color: #3ecf67;
`;

const Timer = styled.div`
  margin-top: 320px;
  display: flex;
  align-items: center;
  justify-content: space-around;
  height: 50px;
`;

const Description = styled.div`
  margin-top: 100px;
  display: flex;
  align-items: center;
  justify-content: space-around;
  height: 50px;
`;

const Options = styled.div`
  margin-top: 300px;
`;

const MyTaskTimer = ({}) => {
  const [authToken, setauthToken] = useState("");
  const { id } = useParams();
  const [task, setTask] = useState(null);
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [timerType, setTimerType] = useState("");
  const [seconds, setSeconds] = useState();
  const [hours, setHours] = useState();
  const [minutes, setMinutes] = useState();
  const [currentSeconds, setCurrentSeconds] = useState();
  const [isRunning, setIsRunning] = useState(true);

  const navigate = useNavigate();

  useEffect(() => {
    const storedToken = sessionStorage.getItem("authToken");

    if (storedToken) {
      setauthToken(storedToken);
    }
  }, []);

  useEffect(() => {
    let timer;

    const config = {
      headers: {
        Authorization: `Bearer ${authToken}`, // Include the authToken in the Authorization header
      },
    };

    if (task == null && authToken) {
      const apiUrl = `http://localhost:8080/api/getTask/${id}`;

      axios
        .get(apiUrl, config)
        .then((response) => {
          const task = response.data;
          setTask(task);
          setName(task.name);
          setDescription(task.description);
          setTimerType(task.timerType);
          setHours(Math.floor(task.seconds / 3600));
          setMinutes(Math.floor((task.seconds % 3600) / 60));
          setSeconds(task.seconds % 60);
          setCurrentSeconds(task.seconds % 60);
        })
        .catch((error) => console.error(error));
    }

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
            setCurrentSeconds(59);
          } else if (currentSeconds === 0) {
            setMinutes((prevMinutes) => prevMinutes - 1);
            setCurrentSeconds(59);
          } else {
            setCurrentSeconds((prevSeconds) => prevSeconds - 1);
          }
          const timerData = {
            id: id,
            seconds: hours * 3600 + minutes * 60 + currentSeconds,
          };

          const config = {
            headers: {
              Authorization: `Bearer ${authToken}`,
            },
          };

          axios
            .post("http://localhost:8080/api/postSeconds", timerData, config)

            .catch((error) => {
              console.error("Error posting timer data:", error);
            });
        }
      }, 1000);
    }

    return () => clearInterval(timer);
  }, [authToken, isRunning, currentSeconds, minutes, hours]);

  const formatTime = (value) => String(value).padStart(2, "0");

  const handleStopTimer = () => {
    navigate(`/my-tasks`, { replace: true });
  };

  return (
    <CenteredContainer>
      <NavBar />
      <TimerContainer>
        <Description>
          <StyledColDesc>
            <BlackSquareDesc>
              <p>Name: {name}</p>
            </BlackSquareDesc>
          </StyledColDesc>
          <StyledColDesc>
            <BlackSquareDesc>
              <p>Timer-Type: {timerType}</p>
            </BlackSquareDesc>
          </StyledColDesc>
        </Description>

        <Timer>
          <Row className="text-center mx-0 d-flex align-items-stretch">
            <div className="text-nowrap">
              <StyledCol>
                <BlackSquare>{formatTime(hours)[0]}</BlackSquare>
              </StyledCol>
              <StyledCol>
                <BlackSquare>{formatTime(hours)[1]}</BlackSquare>
              </StyledCol>
              <StyledColon>:</StyledColon>
              <StyledCol>
                <BlackSquare>{formatTime(minutes)[0]}</BlackSquare>
              </StyledCol>
              <StyledCol>
                <BlackSquare>{formatTime(minutes)[1]}</BlackSquare>
              </StyledCol>
              <StyledColon>:</StyledColon>
              <StyledCol>
                <BlackSquare>{formatTime(currentSeconds)[0]}</BlackSquare>
              </StyledCol>
              <StyledCol>
                <BlackSquare>{formatTime(currentSeconds)[1]}</BlackSquare>
              </StyledCol>
            </div>
          </Row>
        </Timer>

        <Options>
          <Row className="text-center mx-0 mt-3">
            <Col>
              <Button variant="success" onClick={() => setIsRunning(true)}>
                <FaPlay />
              </Button>
            </Col>
            <Col>
              <Button variant="warning" onClick={() => setIsRunning(false)}>
                <FaPause />
              </Button>
            </Col>
            <Col>
              <Button variant="danger" onClick={handleStopTimer}>
                <FaStop />
              </Button>
            </Col>
          </Row>
        </Options>
      </TimerContainer>
    </CenteredContainer>
  );
};

export default MyTaskTimer;
