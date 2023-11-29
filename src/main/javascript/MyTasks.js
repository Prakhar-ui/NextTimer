import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.css";
import { Container, Table, Button } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEdit, faTrash } from "@fortawesome/free-solid-svg-icons";
import NavBar from "./NavBar";
import { useNavigate } from "react-router-dom";
import MyTaskTimer from "./MyTaskTimer";
const MyTasks = () => {
  const [task, setTask] = useState(null);
  const [deleteStatus, setDeleteStatus] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const apiUrl = "http://localhost:8080/api/myTasks";

    fetch(apiUrl)
      .then((response) => response.json())
      .then((task) => setTask(task))
      .catch((error) => console.error(error));
    setDeleteStatus(null);
  }, [deleteStatus]);

  const handleDeleteClick = (id) => {
    const deleteTaskapiUrl = `http://localhost:8080/api/deleteTask/${id}`;

    fetch(deleteTaskapiUrl, {
      method: "DELETE",
    })
      .then((response) => {
        if (response.ok) {
          setDeleteStatus("deleted successfully");
        } else {
          setDeleteStatus("error");
        }
      })
      .catch((error) => {
        setDeleteStatus("error");
        console.error("Error:", error);
      });
  };

  const handleEditTask = (id) => {
    navigate(`/edit-task/${id}`, { replace: true });
  };

  const formatDuration = ({ seconds }) => {
    const hours = Math.floor(seconds / 3600);
    const minutes = Math.floor((seconds % 3600) / 60);
    const remainingSeconds = seconds % 60;
    if (hours > 0) {
      if (minutes > 0) {
        if (remainingSeconds > 0) {
          return `${hours} hours ${minutes} minutes ${remainingSeconds} seconds`;
        }
        return `${hours} hours ${minutes} minutes`;
      }
      if (remainingSeconds > 0) {
        return `${hours} hours ${remainingSeconds} seconds`;
      }
      return `${hours} hours`;
    }

    if (minutes > 0) {
      if (remainingSeconds > 0) {
        return `${minutes} minutes ${remainingSeconds} seconds`;
      }
      return `${minutes} minutes`;
    }

    if (remainingSeconds > 0) {
      return `${remainingSeconds} seconds`;
    }

    return "0 seconds";
  };

  return (
    <div>
      <NavBar />
      <Container className="my-5 col-md-8">
        <Table striped bordered hover responsive className="text-nowrap">
          <thead className="text-center">
            <tr > 
              <th>No. </th>
              <th style={{ width: "400px"}}>Timer</th>
              <th>Name</th>
              <th>Description</th>
              <th>Timer-Type</th>
              <th>Duration</th>
              <th>Priority</th>
              <th>Enabled</th>
              <th>Edit Task</th>
              <th>Delete Task</th>
            </tr>
          </thead>
          <tbody className="text-center">
            {task &&
              task.map((t) => (
                <tr key={t.id}>
                  <td>{t.id}</td>
                  <MyTaskTimer seconds={t.seconds} />
                  <td>{t.name}</td>
                  <td>{t.description}</td>
                  <td>{t.timerType}</td>
                  <td>{formatDuration({ seconds: t.seconds })}</td>
                  <td>{t.priority}</td>
                  <td>
                    {t.enabled ? (
                      <span style={{ color: "green" }}>&#x2713;</span>
                    ) : (
                      <span style={{ color: "red" }}>&#x2717;</span>
                    )}
                  </td>
                  <td>
                    <Button
                      variant="primary"
                      onClick={() => handleEditTask(t.id)}
                    >
                      <FontAwesomeIcon icon={faEdit} />
                    </Button>
                  </td>
                  <td>
                    <Button
                      variant="danger"
                      onClick={() => handleDeleteClick(t.id)}
                    >
                      <FontAwesomeIcon icon={faTrash} />
                    </Button>
                  </td>
                </tr>
              ))}
          </tbody>
        </Table>
        {deleteStatus && (
          <div>
            {deleteStatus === "deleted successfully" ? (
              <div className="alert alert-success">Deleted successfully</div>
            ) : (
              <div className="alert alert-danger">Error deleting task</div>
            )}
          </div>
        )}
      </Container>
    </div>
  );
};

export default MyTasks;
