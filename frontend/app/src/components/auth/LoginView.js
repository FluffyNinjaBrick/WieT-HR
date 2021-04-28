import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import { useForm } from "react-hook-form";
import FormInputErrorMessage from "../utils/FormInputErrorMessage";
import { API_URL } from "../../api/Api";
import { useContext, useState } from "react";
import { Redirect, useHistory } from "react-router";
import { Alert } from "react-bootstrap";
import { useAuth } from "../auth/useAuth";

export default function LoginView() {
  const {
    register,
    formState: { errors },
    handleSubmit,
  } = useForm();

  const auth = useAuth();

  const [error, setError] = useState(false);
  const [showAlert, setShowAlert] = useState(false);

  const history = useHistory();

  const handleLogin = async (formData) => {
    setError(false);
    setShowAlert(false);

    const response = await auth.login(formData.email, formData.password);

    if (!response.ok) {
      setError(true);
      setShowAlert(true);
    } else {
      history.push("/profile");
    }
  };

  if (auth.user) {
    return <Redirect to="/" />;
  }

  return (
    <div className="d-flex flex-column align-items-center justify-content-center mt-5">
      {error && showAlert && (
        <Alert variant="danger" onClose={() => setShowAlert(false)} dismissible>
          Wystąpił błąd.
        </Alert>
      )}
      <h1 className="my-4">Zaloguj się</h1>
      <Form className="AuthForm" onSubmit={handleSubmit(handleLogin)}>
        <Form.Group>
          <Form.Label htmlFor="email">Adres email</Form.Label>
          <Form.Control
            type="email"
            name="email"
            placeholder="Podaj adres email"
            {...register("email", { required: true })}
          />
          {errors.email && (
            <FormInputErrorMessage errorMessage="Adres email jest wymagany" />
          )}
        </Form.Group>

        <Form.Group>
          <Form.Label htmlFor="password">Hasło</Form.Label>
          <Form.Control
            type="password"
            name="password"
            placeholder="Podaj hasło"
            {...register("password", { required: true })}
          />
          {errors.password && (
            <FormInputErrorMessage errorMessage="Hasło jest wymagane" />
          )}
        </Form.Group>

        <Button variant="primary" type="submit" className="w-100 mt-3">
          Log in
        </Button>
      </Form>
    </div>
  );
}
