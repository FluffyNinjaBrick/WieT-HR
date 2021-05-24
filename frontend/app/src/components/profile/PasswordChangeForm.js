import { Form } from "react-bootstrap";
import { useAuth } from "../auth/useAuth";
import Button from "react-bootstrap/Button";
import { useForm } from "react-hook-form";
import FormInputErrorMessage from "../utils/FormInputErrorMessage";
import { useMutation } from "react-query";
import { LoadingComponent } from "../loader/LoadingView";

export default function PasswordChangeForm({ onHide }) {
  const auth = useAuth();

  const {
    register,
    formState: { errors },
    handleSubmit,
    getValues,
  } = useForm();

  const mutationChangePassword = useMutation((newPassword) =>
    auth.changePassword(newPassword)
  );

  const handlePasswordChange = async (formData) => {
    mutationChangePassword.mutate(formData.newPassword);
  };

  if (mutationChangePassword.isLoading) {
    return <LoadingComponent />;
  }

  if (mutationChangePassword.isError) {
    return <div>Wystąpił błąd. Spróbuj ponownie.</div>;
  }

  if (mutationChangePassword.isSuccess) {
    setTimeout(function () {
      onHide();
    }, 1000);
    return <LoadingComponent />;
  }

  return (
    <>
      <Form onSubmit={handleSubmit(handlePasswordChange)}>
        <Form.Group>
          <Form.Label htmlFor="oldPassword">Obecne hasło</Form.Label>
          <Form.Control
            type="password"
            name="oldPassword"
            placeholder="Podaj obecne hasło"
            {...register("oldPassword", { required: true })}
          />
          {errors.oldPassword && (
            <FormInputErrorMessage errorMessage="To pole jest wymagane" />
          )}
        </Form.Group>

        <Form.Group>
          <Form.Label htmlFor="newPassword">Nowe hasło</Form.Label>
          <Form.Control
            type="password"
            name="newPassword"
            placeholder="Podaj nowe hasło"
            {...register("newPassword", { required: true })}
          />
          {errors.newPassword && (
            <FormInputErrorMessage errorMessage="To pole jest wymagane" />
          )}
        </Form.Group>

        <Form.Group>
          <Form.Label htmlFor="newPasswordConfirmation">
            Potwierdź nowe hasło
          </Form.Label>
          <Form.Control
            type="password"
            name="newPasswordConfirmation"
            placeholder="Podaj nowe hasło"
            {...register("newPasswordConfirmation", {
              required: true,
              validate: () =>
                getValues("newPassword") ===
                getValues("newPasswordConfirmation"),
            })}
          />
          {errors.newPasswordConfirmation &&
            errors.newPasswordConfirmation.type === "required" && (
              <FormInputErrorMessage errorMessage="To pole jest wymagane" />
            )}
          {errors.newPasswordConfirmation &&
            errors.newPasswordConfirmation.type === "validate" && (
              <FormInputErrorMessage errorMessage="Hasła muszą być takie same" />
            )}
        </Form.Group>

        <div className="mt-4">
          <Button variant="primary" type="submit" className="mr-3">
            Zmień hasło
          </Button>
          <Button variant="ternary" onClick={onHide}>
            Anuluj
          </Button>
        </div>
      </Form>
    </>
  );
}
