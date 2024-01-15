import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.css";
import styled from "styled-components";
import { Container, Form, Button } from "react-bootstrap";
import "@fortawesome/fontawesome-free/css/all.min.css";
import NavBar from "./NavBar";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";

// Styled Components
const StyledWrapper = styled.div`
  width: 100%;
  height: 100vh;
  overflow: hidden;
  padding-left: 0;
  z-index: -1;
  background: linear-gradient(to bottom right, #ffd9fb, white);
`;

const StyledContainer = styled(Container)`
  border: 1px solid black;
  width: 700px;
`;

const StyledForm = styled(Form)`
  width: 400px;
`;

const EditTask = ({}) => {
  const [authToken, setauthToken] = useState("");
  const { id } = useParams();
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [timerType, setTimerType] = useState("");
  const [hours, setHours] = useState(0);
  const [minutes, setMinutes] = useState(0);
  const [seconds, setSeconds] = useState(0);
  const navigate = useNavigate();

  useEffect(() => {
    const storedToken = sessionStorage.getItem("authToken");
    if (storedToken) {
      setauthToken(storedToken);
    }

  }, []);

  useEffect(() => {
    const config = {
      headers: {
        Authorization: `Bearer ${authToken}`, // Include the authToken in the Authorization header
      },
    };

    const apiUrl = `http://localhost:8080/api/getTask/${id}`;



    axios
      .get(apiUrl, config)
      .then((response) => {
        const task = response.data;
        setName(task.name);
        setDescription(task.description);
        setTimerType(task.timerType);
        setHours(Math.floor(task.seconds / 3600));
        setMinutes(Math.floor((task.seconds % 3600) / 60));
        setSeconds(task.seconds % 60);
      })
      .catch((error) => console.error(error));
  }, [authToken]);

  async function save(task) {
    task.preventDefault();

    const totalSeconds = hours * 3600 + minutes * 60 + seconds;

    const taskData = {
      id,
      name,
      description,
      timerType: timerType,
      seconds: totalSeconds,
    };

    try {
      const config = {
        headers: {
          Authorization: `Bearer ${authToken}`, // Include the authToken in the Authorization header
        },
      };

      await axios.post("/api/editTask", taskData, config);
      navigate("/my-tasks");
      alert("Task Edited successfully");
    } catch (error) {
      console.error("Error while editing task:", error);
    }
  }

  return (
    <StyledWrapper>
      <NavBar />
      <StyledContainer className="my-5 p-5">
        <h4 className="text-center">Edit Task</h4>
        <StyledForm className="col-md-6 offset-md-3" onSubmit={save}>
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
              type="text"
              name="description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              required
            />
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

          <Form.Group className="mb-3" style={{ width: "400px" }}>
            <Form.Label className="fw-bold">Timer</Form.Label>

            <div className="d-flex align-items-center">
              <Form.Label className="fw-bold me-2">Hours</Form.Label>
              <Form.Control
                type="number"
                name="hours"
                value={hours}
                onChange={(e) => setHours(parseInt(e.target.value, 10))}
                min="0"
                placeholder="Hours"
                className="me-2"
              />

              <Form.Label className="fw-bold me-2">Minutes</Form.Label>
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

              <Form.Label className="fw-bold me-2">Seconds</Form.Label>
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
              Save Changes
            </Button>
          </div>
        </StyledForm>
      </StyledContainer>
    </StyledWrapper>
  );
};

export default EditTask;
