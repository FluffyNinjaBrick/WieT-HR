import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import { useForm } from "react-hook-form";
import FormInputErrorMessage from "../utils/FormInputErrorMessage";
import SubordinateEmployeeList from "./SubordinateEmployeeList";
import { useEffect, useState } from "react";
import { API_URL } from "../../api/Api";
import { Redirect, useHistory, useLocation } from "react-router";
import { getCurrentUser } from "../../services/AuthService";
import { Loading } from "../loader/LoadingView";

export default function EmployeeEditForm() {
  const {
    register,
    formState: { errors },
    handleSubmit,
  } = useForm();

  let location = useLocation();
  const history = useHistory();
  const [managedEmployees, setManagedEmployees] = useState([]);
  const [employees, setEmployees] = useState([]);
  const employeeToEdit = (location.state && location.state.employee) || {};
  const [submitSuccessful, setSubmitSuccessful] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    const user = JSON.parse(getCurrentUser());
    const token = user ? user.jwt : "";
    const auth = "Bearer " + token;

    const fetchEmployees = async () => {
      const response = await fetch(API_URL + "employees", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: auth,
        },
      });

      const data = await response.json();

      let filteredEmployees = data;
      if (employeeToEdit.hasOwnProperty("permissions")) {
        setManagedEmployees(employeeToEdit.permissions.managedUsers);
        filteredEmployees = data.filter(
          (item) =>
            !employeeToEdit.permissions.managedUsers
              .map((e) => e.id)
              .includes(item.id)
        );
      }
      if (employeeToEdit) {
        filteredEmployees = filteredEmployees.filter(
          (e) => e.id !== employeeToEdit.id
        );
      }

      setEmployees(filteredEmployees);
    };

    fetchEmployees();
  }, []);

  const moveToManaged = (employee) => {
    setManagedEmployees([...managedEmployees, employee]);
    setEmployees(employees.filter((e) => e.id !== employee.id));
  };

  const moveToUnmanaged = (employee) => {
    setEmployees([...employees, employee]);
    setManagedEmployees(managedEmployees.filter((e) => e.id !== employee.id));
  };

  const createEmployee = async (formData) => {
    var generator = require("generate-password");

    var password = generator.generate({
      length: 10,
      numbers: true,
    });

    const permissions = formData.permissions;

    const data = {
      ...formData,
      permissionHelper: {
        ...permissions,
        managedUsers: managedEmployees.map((employee) => employee.id),
      },
      status: "WORKING",
      password: password,
    };

    const user = JSON.parse(getCurrentUser());
    const token = user ? user.jwt : "";
    const auth = "Bearer " + token;

    setSubmitting(true);

    await fetch(API_URL + "employees/create", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: auth,
      },
      body: JSON.stringify(data),
    }).then(() => {
      setSubmitSuccessful(true);
      setSubmitting(false);
    });
  };

  const handleCancel = () => {
    history.push("/employees");
  };

  if (submitting) {
    return <Loading />;
  }

  if (submitSuccessful) {
    return <Redirect to="/employees" />;
  }

  return (
    <div className="container col-lg-8 col-md-10 col-sm-12 my-5">
      <h3>Dane personalne</h3>
      <Form onSubmit={handleSubmit(createEmployee)}>
        <Form.Group>
          <Form.Label htmlFor="firstName">Imię</Form.Label>
          <Form.Control
            type="text"
            name="firstName"
            placeholder="Podaj imię"
            defaultValue={employeeToEdit.firstName}
            {...register("firstName", { required: true })}
          />
          {errors.firstName && (
            <FormInputErrorMessage errorMessage="Imię jest wymagane" />
          )}
        </Form.Group>
        <Form.Group>
          <Form.Label htmlFor="lastName">Nazwisko</Form.Label>
          <Form.Control
            type="text"
            name="lastName"
            placeholder="Podaj nazwisko"
            defaultValue={employeeToEdit.lastName}
            {...register("lastName", { required: true })}
          />
          {errors.lastName && (
            <FormInputErrorMessage errorMessage="Nazwisko jest wymagane" />
          )}
        </Form.Group>
        <h3 className="mt-5 mb-3">Dane kontaktowe</h3>
        <Form.Group>
          <Form.Label htmlFor="address">Adres zamieszkania</Form.Label>
          <Form.Control
            type="text"
            name="address"
            placeholder="Podaj adres zamieszkania"
            defaultValue={employeeToEdit.address}
            {...register("address", { required: true })}
          />
          {errors.address && (
            <FormInputErrorMessage errorMessage="Adres zamieszkania jest wymagany" />
          )}
        </Form.Group>
        <Form.Group>
          <Form.Label htmlFor="phone">Numer telefonu</Form.Label>
          <Form.Control
            type="text"
            name="phone"
            placeholder="Podaj numer telefonu"
            defaultValue={employeeToEdit.phone}
            {...register("phone", { required: true })}
          />
          {errors.phone && (
            <FormInputErrorMessage errorMessage="Numer telefonu jest wymagany" />
          )}
        </Form.Group>
        <Form.Group>
          <Form.Label htmlFor="email">Adres e-mail</Form.Label>
          <Form.Control
            type="email"
            name="email"
            placeholder="Podaj adres e-mail"
            defaultValue={employeeToEdit.email}
            {...register("email", { required: true })}
          />
          {errors.email && (
            <FormInputErrorMessage errorMessage="Adres e-mail jest wymagany" />
          )}
        </Form.Group>
        <h3 className="mt-5 mb-3">Uprawnienia (opcjonalne)</h3>
        <Form.Group>
          <Form.Check
            type="checkbox"
            name="addUsers"
            label="Możliwość dodawania pracowników (opcjonalne)"
            {...register("permissions.addUsers", { required: false })}
          />
        </Form.Group>
        <Form.Group>
          <Form.Check
            type="checkbox"
            name="modifyBonusBudget"
            label="Możliwość edycji budżetu premiowego (opcjonalne)"
            {...register("permissions.modifyBonusBudget", { required: false })}
          />
        </Form.Group>

        <h3 className="mt-5 mb-3">Podwładni (opcjonalne)</h3>
        <div className="container border row m-0 py-3 px-0">
          <div className="col-md-6 text-center">
            <h4 className="mb-4">Wybrani</h4>
            <SubordinateEmployeeList
              employees={managedEmployees}
              onChange={moveToUnmanaged}
              managed={true}
            />
          </div>

          <div className="col-md-6 text-center">
            <h4 className="mb-4">Niewybrani</h4>
            <SubordinateEmployeeList
              employees={employees}
              onChange={moveToManaged}
              managed={false}
            />
          </div>
        </div>

        <div>
          <Button className="mt-5 mr-3" variant="primary" type="submit">
            {employeeToEdit.hasOwnProperty("firstName")
              ? "Zapisz zmiany"
              : "Dodaj pracownika"}
          </Button>
          <Button
            className="mt-5"
            variant="secondary"
            onClick={() => handleCancel()}
          >
            Anuluj
          </Button>
        </div>
      </Form>
    </div>
  );
}
