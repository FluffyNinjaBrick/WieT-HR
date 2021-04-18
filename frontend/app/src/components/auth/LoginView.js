import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import { useForm } from "react-hook-form";
import FormInputErrorMessage from "../utils/FormInputErrorMessage";
import { API_URL } from "../../api/Api";
import { useContext, useState } from "react";
import { useHistory } from "react-router";
import { Alert } from "react-bootstrap";
import { getCurrentUser, login } from "../../services/AuthService";

export default function LoginView() {
  const {
    register,
    formState: { errors },
    handleSubmit,
  } = useForm();

  const [error, setError] = useState(false);
  const [showAlert, setShowAlert] = useState(false);

  const history = useHistory();

  const handleLogin = async (formData) => {
    setError(false);
    setShowAlert(false);

    const response = await login(formData);

    if (!response.ok) {
      setError(true);
      setShowAlert(true);
    } else {
      history.push("/profile");
    }
  };

  return (
    <div className="d-flex flex-column align-items-center justify-content-center mt-5">
      {error && showAlert && (
        <Alert variant="danger" onClose={() => setShowAlert(false)} dismissible>
          Wystąpił błąd.
        </Alert>
      )}
      <Form className="AuthForm" onSubmit={handleSubmit(handleLogin)}>
        <Form.Group>
          <Form.Label htmlFor="email">Email address</Form.Label>
          <Form.Control
            type="email"
            name="email"
            placeholder="Enter email"
            {...register("email", { required: true })}
          />
          {errors.email && (
            <FormInputErrorMessage errorMessage="Email is required" />
          )}
        </Form.Group>

        <Form.Group>
          <Form.Label htmlFor="password">Password</Form.Label>
          <Form.Control
            type="password"
            name="password"
            placeholder="Password"
            {...register("password", { required: true })}
          />
          {errors.password && (
            <FormInputErrorMessage errorMessage="Password is required" />
          )}
        </Form.Group>

        <Button variant="primary" type="submit">
          Log in
        </Button>
      </Form>
    </div>
  );
}
