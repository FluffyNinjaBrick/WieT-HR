import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import { useForm } from "react-hook-form";
import FormInputErrorMessage from "../utils/FormInputErrorMessage";
import { useEffect, useState } from "react";
import { Redirect, useHistory, useLocation } from "react-router";
import { getCurrentUser } from "../../services/AuthService";
import { Loading } from "../loader/LoadingView";
import { postNewContract } from "../../services/DocumentsService";

export default function AddContractForm({employee}) {
  const {
    register,
    formState: { errors },
    handleSubmit,
    getValues,
  } = useForm();

  let location = useLocation();
  const history = useHistory();
  const employeeToEdit = (location.state && location.state.employee) || {};
  const [submitSuccessful, setSubmitSuccessful] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    const user = JSON.parse(getCurrentUser());
    const token = user ? user.jwt : "";
    const auth = "Bearer " + token;
    console.log(employee.id)
  }, []);


  const employeeId = employee.id;

  const createContract = async (formData) => {
    setSubmitting(true);

    postNewContract(formData, employeeId);

    setSubmitSuccessful(true);
    setSubmitting(false);
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
      <Form onSubmit={handleSubmit(createContract)}>
        
      <Form.Group>
            <Form.Label htmlFor="Date">Początek kontraktu</Form.Label>
            <Form.Control
              type="date"
              name="dateFrom"
              id="dateFrom"
              {...register("dateFrom", {
                required: true,
                validate: () => getValues("dateFrom") <= getValues("dateTo"),
              })}
            />
            {errors.dateFrom && errors.dateFrom.type === "required" && (
              <FormInputErrorMessage errorMessage="Data jest wymagana" />
            )}
            {errors.dateFrom && errors.dateFrom.type === "validate" && (
              <FormInputErrorMessage errorMessage="Data początku kontraktu nie może być później niż data końca." />
            )}
          </Form.Group>
          <Form.Group>
            <Form.Label htmlFor="Date">Koniec kontraktu</Form.Label>
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
              <FormInputErrorMessage errorMessage="Data końca kontraktu nie może być wcześniej niż data początku." />
            )}
          </Form.Group>
      <Form.Group>
          <Form.Label htmlFor="salary">Wynagrodzenie (PLN)</Form.Label>
          <Form.Control
            type="number"
            name="firstName"
            placeholder="Podaj wartość wynagrodzenia"
            {...register("salary", { required: true })}
          />
          {errors.firstName && (
            <FormInputErrorMessage errorMessage="Wynagrodzenie jest wymagane" />
          )}
        </Form.Group>
        <Form.Group>
          <Form.Label htmlFor="dutyAllowance">Wysługa lat (PLN)</Form.Label>
          <Form.Control
            type="number"
            name="dutyAllowance"
            placeholder="Podaj wartość wysługi lat"
            {...register("dutyAllowance", { required: true })}
          />
          {errors.firstName && (
            <FormInputErrorMessage errorMessage="Wysługa lat jest wymagana" />
          )}
        </Form.Group>
        <Form.Group>
          <Form.Label htmlFor="workingHours">Ilość roboczogodzin w miesiącu</Form.Label>
          <Form.Control
            type="number"
            name="workingHours"
            placeholder="Podaj ilość roboczogodzin w miesiącu"
            {...register("workingHours", { required: true })}
          />
          {errors.firstName && (
            <FormInputErrorMessage errorMessage="Czas pracy jest wymagany" />
          )}
        </Form.Group>
        <Form.Group>
          <Form.Label htmlFor="salary">Przysługujący urlop (w dniach)</Form.Label>
          <Form.Control
            type="number"
            name="annualLeaveDays"
            placeholder="Podaj ilość dni urlopowych"
            {...register("annualLeaveDays", { required: true })}
          />
          {errors.firstName && (
            <FormInputErrorMessage errorMessage="Dni urlopowe są wymagane" />
          )}
        </Form.Group>
        <Form.Group>
              <Form.Label htmlFor="leaveType">Typ kontraktu</Form.Label>
              <Form.Control
                as="select"
                defaultValue="Wybierz typ kontraktu"
                name="type"
                id="type"
                {...register("type", { required: true })}
              >
                <option value="MANDATE">Umowa zlecenie</option>
                <option value="EMPLOYMENT">Umowa o pracę</option>
                <option value="WORK">Umowa o dzieło</option>
              </Form.Control>
            </Form.Group>

{/* MANDATE, EMPLOYMENT, WORK */}

       
        <div>
          <Button className="mt-5 mr-3" variant="primary" type="submit">
            Dodaj kontrakt
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
