import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.css";
import { Container, Form, Button } from "react-bootstrap";
import "@fortawesome/fontawesome-free/css/all.min.css";
import NavBar from "./NavBar";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";

const EditTask = () => {
  const { id } = useParams();
  const [task, setTask] = useState({});
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [timerType, setTimerType] = useState("");
  const [hours, setHours] = useState(0);
  const [minutes, setMinutes] = useState(0);
  const [seconds, setSeconds] = useState(0);
  const [priority, setPriority] = useState("");
  const [enabled, setEnabled] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const apiUrl = `http://localhost:8080/api/getTask/${id}`;

    fetch(apiUrl)
      .then((response) => response.json())
      .then((task) => {
        setTask(task);
        setName(task.name);
        setDescription(task.description);
        setTimerType(task.timerType);
        console.log(timerType);
        setHours(task.seconds / 3600);
        setMinutes((task.seconds % 3600) / 60);
        setSeconds(task.seconds % 60);
        setPriority(task.priority);
        setEnabled(task.enabled);
      })
      .catch((error) => console.error(error));
  }, [id]);

  async function save(task) {
    task.preventDefault();

    const totalSeconds = hours * 3600 + minutes * 60 + seconds;

    const taskData = {
      id,
      name,
      description,
      timerType: timerType,
      seconds: totalSeconds,
      priority: parseInt(priority),
      enabled: Boolean(enabled),
    };

    console.log(timerType);

    try {
      await axios.post("/api/editTask", taskData);
      navigate("/my-tasks");
      alert("Task Edited successfully");
    } catch (error) {
      console.error("Error while editing task:", error);
    }
  }

  return (
    <div>
      <NavBar />
      <Container className="my-5 p-5" style={{ border: "1px solid black" }}>
        <h4 className="text-center">Edit Task</h4>
        <Form className="col-md-6 offset-md-3" onSubmit={save}>
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

          <Form.Group className="mb-3">
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

          <Form.Group className="mb-3">
            <Form.Label className="fw-bold">Priority</Form.Label>
            <Form.Control
              type="text"
              name="priority"
              value={priority}
              onChange={(e) => setPriority(e.target.value)}
              required
            />
          </Form.Group>

          <Form.Group className="mb-3 d-flex align-items-center">
            <Form.Check
              type="checkbox"
              label="Enabled"
              className="fw-bold"
              name="enabled"
              checked={enabled}
              onChange={(e) =>
                setEnabled(e.target.checked ? e.target.checked : false)
              }
            />
          </Form.Group>

          <div className="text-center">
            <Button variant="primary" type="submit">
              Save Changes
            </Button>
          </div>
        </Form>
      </Container>
    </div>
  );
};

export default EditTask;
