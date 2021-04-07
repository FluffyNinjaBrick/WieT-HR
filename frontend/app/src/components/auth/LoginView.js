import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import { useForm } from "react-hook-form";
import FormInputErrorMessage from "../utils/FormInputErrorMessage";

export default function LoginView() {
  const {
    register,
    formState: { errors },
    handleSubmit,
  } = useForm();

  const handleLogin = (formData) => {
    console.log(formData);
    //TODO: post data
  };

  return (
    <div className="AuthFormContainer">
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
