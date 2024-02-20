import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.css";
import styled from "styled-components";
import { Container, Form, Button } from "react-bootstrap";
import "@fortawesome/fontawesome-free/css/all.min.css";
import NavBar from "./NavBar";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const Wrapper = styled.div`
  width: 100%;
  height: 100%;
  object-fit: cover;
  position: fixed;
  top: 0;
  left: 0;
  overflow: auto;
  padding-left: 0;
  z-index: -1;
  background: linear-gradient(to bottom right, #ffd9fb, white);
`;

const StyledContainer = styled(Container)`
  border: 1px solid black;
  max-width: 90%;
  width: 600px;
  margin: auto;
`;

const StyledForm = styled(Container)`
  max-width: 90%;
  width: 400px;
  margin: auto;
`;

const CreateTask = ({}) => {
  const [authToken, setauthToken] = useState("");
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [timerType, setTimerType] = useState("");
  const [hours, setHours] = useState(0);
  const [minutes, setMinutes] = useState(0);
  const [seconds, setSeconds] = useState(0);
  const [countChars, setCountChars] = useState(0);
  const navigate = useNavigate();

  const config = {
    headers: {
      Authorization: `Bearer ${authToken}`,
    },
  };

  async function save(task) {
    task.preventDefault();

    const totalSeconds = hours * 3600 + minutes * 60 + seconds;

    const taskData = {
      name,
      description,
      timerType: timerType,
      seconds: totalSeconds,
    };

    try {
      await axios.post("/api/newTask", taskData, config);
      navigate("/my-tasks");
      alert("Task created successfully");

      setName("");
      setDescription("");
      setTimerType("");
      setHours(0);
      setMinutes(0);
      setSeconds(0);
    } catch (error) {
      console.error("Error while saving task:", error);
    }
  }

  useEffect(() => {
    const storedToken = sessionStorage.getItem("authToken");
    if (storedToken) {
      setauthToken(storedToken);
    }
  }, [authToken]);

  useEffect(() => {
    // Count spaces when description changes
    const countCharsFunction = (text) => {
      return text.length;
    };
    setCountChars(countCharsFunction(description));
  }, [description]);

  return (
    <Wrapper>
      <NavBar />
      <StyledContainer className="my-5 p-5">
        <h4 className="text-center">Create Task</h4>
        <StyledForm className="col-md-6 offset-md-3">
          <Form onSubmit={save}>
            <Form.Group className="mb-3">
              <Form.Label className="fw-bold">Name</Form.Label>
              <Form.Control
                type="text"
                name="name"
                value={name}
                onChange={(e) => setName(e.target.value)}
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label className="fw-bold">Description</Form.Label>
              <Form.Control
                as="textarea"
                rows={2}
                maxLength={150}
                value={description}
                onChange={(e) => {
                  setDescription(e.target.value);
                  setCountChars(e.target.value.trim().length);
                }}
                style={{ resize: "none" }}
                required
              />
              <div style={{ textAlign: "right", fontSize: "12px" }}>
                {countChars}/150 chars
              </div>
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label className="fw-bold">Timer Type</Form.Label>
              <Form.Select
                name="timerType"
                value={timerType}
                onChange={(e) => setTimerType(e.target.value)}
                required
              >
                <option value="">Select Timer Type</option>
                <option value="One-Time">One-Time</option>
                <option value="Daily">Daily</option>
              </Form.Select>
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label className="fw-bold">Timer</Form.Label>

              <div>
                <Form.Label className="fw-bold">Hours</Form.Label>
                <Form.Control
                  type="number"
                  name="hours"
                  value={hours}
                  onChange={(e) => setHours(parseInt(e.target.value, 10))}
                  min="0"
                  placeholder="Hours"
                  className="me-2"
                />
              </div>

              <div>
                <Form.Label className="fw-bold">Minutes</Form.Label>
                <Form.Control
                  type="number"
                  name="minutes"
                  value={minutes}
                  onChange={(e) => setMinutes(parseInt(e.target.value, 10))}
                  min="0"
                  max="59"
                  placeholder="Minutes"
                  className="me-2"
                />
              </div>

              <div>
                <Form.Label className="fw-bold">Seconds</Form.Label>
                <Form.Control
                  type="number"
                  name="seconds"
                  value={seconds}
                  onChange={(e) => setSeconds(parseInt(e.target.value, 10))}
                  min="0"
                  max="59"
                  placeholder="Seconds"
                />
              </div>
            </Form.Group>
            <div className="text-center">
              <Button variant="primary" type="submit">
                Submit
              </Button>
            </div>
          </Form>
        </StyledForm>
      </StyledContainer>
    </Wrapper>
  );
};

export default CreateTask;
