import { useState } from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import { Alert } from "react-bootstrap";
import { useForm } from "react-hook-form";
import FormInputErrorMessage from "../utils/FormInputErrorMessage";
import React from "react";
import { useHistory } from "react-router";
import { API_URL } from "../../api/Api";


export default function DelegationCreateForm() {
  const {
    register,
    formState: { errors },
    handleSubmit, getValues
  } = useForm();

  const [error, setError] = useState(false);
  const [showAlert, setShowAlert] = useState(false);

  const history = useHistory();

  const handleCancel = () => {
    history.push("/delegations");
  };

  const handleFormSubmit = async (formData) => {
    const response = await fetch(API_URL + "documents/create/delegation", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        //todo add authentication token
      },
      body: JSON.stringify(formData),
    });
    if (!response.ok) {
      setError(true);
      setShowAlert(true);
    } else {
      history.push("/leaves");
    }

    console.log(formData);
  };

  return (
    <div className="d-flex flex-column align-items-center justify-content-center mt-5">
      <h1>Wypełnij wniosek o delegację</h1>
      <div className="mt-5">
        {error && showAlert &&
          <Alert variant="danger" onClose={() => setShowAlert(false)} dismissible>
            Wystąpił błąd.
          </Alert>
        }
        <Form className="LeavesForm" onSubmit={handleSubmit(handleFormSubmit)}>
          <Form.Group>
            <Form.Label htmlFor="employeeID">Tymczasowe ID pracownika</Form.Label>
            <Form.Control
              type="employeeID"
              name="employeeID"
              placeholder="Podaj ID pracownika"
              {...register("employeeID", { required: true })}
            />
            {errors.employeeID && (
              <FormInputErrorMessage errorMessage="ID jest wymagane" />
            )}
          </Form.Group>
          <Form.Group>
            <Form.Label htmlFor="Date">Początek delegacji</Form.Label>
            <Form.Control
              type="date"
              name="dateFrom"
              id="dateFrom"
              {...register("dateFrom", { required: true, validate: () => getValues("dateFrom") <= getValues("dateTo") })}
            />
            {errors.dateTo && errors.dateTo.type === "required" && (
              <FormInputErrorMessage errorMessage="Data jest wymagana" />
            )}
            {errors.dateFrom && errors.dateFrom.type === "validate" && (
              <FormInputErrorMessage errorMessage="Data początku delegacji nie może być później niż data końca." />
            )}
          </Form.Group>
          <Form.Group>
            <Form.Label htmlFor="Date">Koniec delegacji</Form.Label>
            <Form.Control
              type="date"
              name="dateTo"
              id="dateTo"
              {...register("dateTo", { required: true, validate: () => getValues("dateFrom") <= getValues("dateTo") })}
            />
            {errors.dateTo && errors.dateTo.type === "required" && (
              <FormInputErrorMessage errorMessage="Data jest wymagana" />
            )}
            {errors.dateTo && errors.dateTo.type === "validate" && (
              <FormInputErrorMessage errorMessage="Data końca delegacji nie może być wcześniej niż data początku." />
            )}
          </Form.Group>
          <Form.Group>
            <Form.Label htmlFor="destination">Miejsce delegacji</Form.Label>
            <Form.Control
              type="text"
              name="destination"
              placeholder="Podaj miejsce delegacji"
              {...register("destination", { required: true })}
            />
            {errors.destination && (
              <FormInputErrorMessage errorMessage="Miejsce docelowe delegacji jest wymagane" />
            )}
          </Form.Group>
          <Button className="mt-3 mr-3" variant="primary" type="submit">
            Złóż wniosek
          </Button>
          <Button
            className="mt-3 "
            variant="secondary"
            onClick={() => handleCancel()}
          >
            Anuluj
          </Button>
        </Form>

      </div>
    </div>

  );



}