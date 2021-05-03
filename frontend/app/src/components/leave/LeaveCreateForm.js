import { useState } from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import { Alert } from "react-bootstrap";
import { useForm } from "react-hook-form";
import FormInputErrorMessage from "../utils/FormInputErrorMessage";
import React from "react";
import { useHistory } from "react-router";
import { API_URL } from "../../api/Api";
import { getCurrentUser } from "../../services/AuthService";
import { Loading } from "../loader/LoadingView";

export default function LeaveCreateForm() {
  const {
    register,
    formState: { errors },
    handleSubmit,
    getValues,
  } = useForm();

  const [error, setError] = useState(false);
  const [showAlert, setShowAlert] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  const token = JSON.parse(getCurrentUser()).jwt;
  const id = JSON.parse(getCurrentUser()).id;
  const auth = "Bearer " + token;

  const history = useHistory();

  const handleCancel = () => {
    history.push("/leaves");
  };

  const handleFormSubmit = async (formData) => {
    setError(false);
    setShowAlert(false);

    const data = {
      id: id,
      ...formData,
    };

    setSubmitting(true);

    const response = await fetch(API_URL + "documents/create/daysoff", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: auth,
      },
      body: JSON.stringify(data),
    });

    setSubmitting(false);

    if (!response.ok) {
      setError(true);
      setShowAlert(true);
    } else {
      history.push("/leaves");
    }

    console.log(data);
  };

  if (submitting) {
    return <Loading />;
  }

  return (
    <div className="mt-5">
      <div className="container text-center">
        <h1>Wypełnij wniosek o urlop</h1>
      </div>
      <div className="container col-lg-4 col-md-6 col-sm-8 mt-5">
        {error && showAlert && (
          <Alert
            variant="danger"
            onClose={() => setShowAlert(false)}
            dismissible
          >
            Wystąpił błąd.
          </Alert>
        )}

        <div className="mt-5">
          <Form onSubmit={handleSubmit(handleFormSubmit)}>
            <Form.Group>
              <Form.Label htmlFor="Date">Początek urlopu</Form.Label>
              <Form.Control
                type="date"
                name="dateFrom"
                id="dateFrom"
                {...register("dateFrom", {
                  required: true,
                  validate: () => getValues("dateFrom") <= getValues("dateTo"),
                })}
              />
              {errors.dateTo && errors.dateTo.type === "required" && (
                <FormInputErrorMessage errorMessage="Data jest wymagana" />
              )}
              {errors.dateTo && errors.dateTo.type === "validate" && (
                <FormInputErrorMessage errorMessage="Data początku urlopu nie może być później niż data końca." />
              )}
            </Form.Group>
            <Form.Group>
              <Form.Label htmlFor="Date">Koniec urlopu</Form.Label>
              <Form.Control
                type="date"
                name="dateTo"
                id="dateTo"
                {...register("dateTo", {
                  required: true,
                  validate: () => getValues("dateFrom") <= getValues("dateTo"),
                })}
              />
              {errors.dateTo && errors.dateTo.type === "required" && (
                <FormInputErrorMessage errorMessage="Data jest wymagana" />
              )}
              {errors.dateTo && errors.dateTo.type === "validate" && (
                <FormInputErrorMessage errorMessage="Data końca urlopu nie może być wcześniej niż data początku." />
              )}
            </Form.Group>
            <Form.Group>
              <Form.Label htmlFor="leaveType">Typ urlopu</Form.Label>
              <Form.Control
                as="select"
                defaultValue="Wybierz typ urlopu"
                name="leaveType"
                id="leaveType"
                {...register("leaveType", { required: true })}
              >
                <option value="MATERNITY">Macierzyński</option>
                <option value="SICK">Chorobowy</option>
                <option value="RECREATIONAL">Rekreacyjny</option>
              </Form.Control>
            </Form.Group>
            <Button className="mt-3 mr-3" variant="primary" type="submit">
              Złóż wniosek
            </Button>
            <Button
              className="mt-3"
              variant="secondary"
              onClick={() => handleCancel()}
            >
              Anuluj
            </Button>
          </Form>
        </div>
      </div>
    </div>
  );
}
