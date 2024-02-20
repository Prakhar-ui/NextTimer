import React, { useEffect, useState } from "react";
import styled from "styled-components";
import "bootstrap/dist/css/bootstrap.css";
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

const EditProfile = ({}) => {
  const [authToken, setauthToken] = useState("");
  const [globalId, setglobalId] = useState("");
  const [name, setName] = useState("");
  const [age, setAge] = useState("");
  const [oldEmail, setOldEmail] = useState("");
  const [email, setEmail] = useState("");
  const [old_password, setOld_Password] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [changePassword, setChangePassword] = useState(false);

  function toggleChangePassword() {
    setChangePassword(!changePassword);
  }

  const navigate = useNavigate();

  useEffect(() => {
    const storedToken = sessionStorage.getItem("authToken");
    const storedglobalId = sessionStorage.getItem("globalId");

    if (storedToken) {
      setauthToken(storedToken);
      setglobalId(storedglobalId);
    }
  }, []);

  useEffect(() => {
    const config = {
      headers: {
        Authorization: `Bearer ${authToken}`, // Include the authToken in the Authorization header
      },
    };

    // Check if globalId is non-null and non-empty
    if (authToken && globalId) {
      const apiUrl = `http://localhost:8080/getUserById?id=${globalId}`;

      axios
        .get(apiUrl, config)
        .then((response) => {
          const user = response.data;
          setName(user.name);
          setAge(user.age);
          setEmail(user.email);
          setOldEmail(user.email);
          setPassword(user.password);
        })
        .catch((error) => console.error(error));
    }
  }, [globalId, authToken]);

  function checkPasswordEquality() {
    return password === confirmPassword;
  }

  async function save(event) {
    event.preventDefault(); // Prevent the default form submission

    const updatedPassword = changePassword ? password : null;

    if (changePassword) {
      try {
        const passwordCheckResult = await checkOldPassword();

        if (!passwordCheckResult) {
          alert("Incorrect Old Password");
          return;
        }

        if (!checkPasswordEquality()) {
          alert("New password and confirm password do not match.");
          return;
        }

        if (old_password === password) {
          alert("Old Password and New Password Cannot be the same.");
          return;
        }
      } catch (error) {
        console.error("Error during password check", error);
        return;
      }
    }

    try {
      // If checkOldPassword passes or no password change is required, proceed with the edit

      const passwordRegex =
        /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+])[A-Za-z\d!@#$%^&*()_+]{8,}$/;

      if (parseInt(age) > 100 || parseInt(age) <= 0 || parseInt(age) == "") {
        alert("Please enter a valid age.");
        return;
      }

      // Name length validation
      if (name.length > 50) {
        alert("Name must be less than 50 characters.");
        return;
      }

      // Email format validation
      const emailRegex = /\S+@\S+\.\S+/;
      if (!emailRegex.test(email)) {
        alert("Please enter a valid email address.");
        return;
      }

      if (changePassword) {
        if (password.length < 8) {
          alert("Password must be at least 8 characters long.");
          return;
        }

        // Password complexity validation
        if (!passwordRegex.test(password)) {
          alert(
            "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character."
          );
          return;
        }
      }

      const userData = {
        id: globalId,
        name: name,
        age: age,
        email: email,
        password: updatedPassword,
      };

      const config = {
        headers: {
          Authorization: `Bearer ${authToken}`, // Include the authToken in the Authorization header
        },
      };

      await axios.post("/editUser", userData, config);

      navigate("/");

      alert("User Edited successfully");
    } catch (error) {
      console.error("Error while editing User:", error);
    }
  }

  async function checkOldPassword() {
    try {
      const userData = {
        username: oldEmail,
        password: old_password,
      };
      console.log(userData);

      const response = await axios.post("/login", userData);

      if (response.data === "Password not matching") {
        return false; // Password check successful
      } else if (response.data === "Login Success") {
        return true;
      }
    } catch (error) {
      console.error("checkOldPassword failing", error);
      // Password check failed
    }
  }

  return (
    <Wrapper>
      <NavBar />
      <StyledContainer className="my-5 p-5">
        <h4 className="text-center">Edit Profile</h4>
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
              <Form.Label className="fw-bold">Age</Form.Label>
              <Form.Control
                type="text"
                name="age"
                value={age}
                onChange={(e) => setAge(e.target.value)}
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label className="fw-bold">Email</Form.Label>
              <Form.Control
                type="email"
                name="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label className="fw-bold">Change Password</Form.Label>
              <Button
                variant="secondary"
                className="ms-2"
                onClick={toggleChangePassword}
              >
                {changePassword ? "Cancel" : "Change Password"}
              </Button>
            </Form.Group>
            {changePassword && (
              <>
                <Form.Group className="mb-3">
                  <Form.Label className="fw-bold">Old Password</Form.Label>
                  <Form.Control
                    type="password"
                    name="oldPassword"
                    onChange={(e) => setOld_Password(e.target.value)}
                  />
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label className="fw-bold">New Password</Form.Label>
                  <Form.Control
                    type="password"
                    name="newPassword"
                    onChange={(e) => setPassword(e.target.value)}
                  />
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label className="fw-bold">Confirm Password</Form.Label>
                  <Form.Control
                    type="password"
                    name="confirmPassword"
                    onChange={(e) => setConfirmPassword(e.target.value)}
                  />
                </Form.Group>
              </>
            )}
            <div className="text-center">
              <Button variant="primary" type="submit">
                Save Changes
              </Button>
            </div>
          </Form>
        </StyledForm>
      </StyledContainer>
    </Wrapper>
  );
};

export default EditProfile;
