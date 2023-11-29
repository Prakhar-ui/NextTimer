import React from "react";
import "bootstrap/dist/css/bootstrap.css";
import { Navbar, Container, Nav } from "react-bootstrap";
import { Link } from "react-router-dom";

const NavBar = () => {
  const navLinkStyle = {
    color: "#fd18fe",
    fontSize: "1.25rem", // Adjust the font size as needed
  };

  return (
    <Navbar
      bg="dark"
      expand="lg"
      variant="dark"
      style={{
        paddingTop: "0",
        paddingBottom: "0",
      }}
    >
      <img src="/images/bg.png" width="100px" alt="Background" />
      <Container fluid>
        <Navbar.Brand as={Link} to="/" className="me-auto" style={navLinkStyle}>
          Home
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="navbarSupportedContent" />
        <Navbar.Collapse id="navbarSupportedContent">
          <Nav className="me-auto">
            <Nav.Link as={Link} to="/my-tasks" style={navLinkStyle}>
              My Tasks
            </Nav.Link>
          </Nav>
          <Nav className="ms-auto">
            <Nav.Link as={Link} to="/create-task" style={navLinkStyle}>
              Create New Task
            </Nav.Link>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default NavBar;
