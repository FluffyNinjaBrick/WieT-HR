import { Form } from "react-bootstrap";
import { useState } from "react";
import { useAuth } from "../../auth/useAuth";
import Button from "react-bootstrap/Button";
import { useForm } from "react-hook-form";
import FormInputErrorMessage from "../../utils/FormInputErrorMessage";
import { useMutation } from "react-query";
import { LoadingComponent } from "../../loader/LoadingView";
import { addBonus } from "../../../services/EmployeeService";
import RangeSlider from "react-bootstrap-range-slider";

export default function AddBonusForm({
  onHide,
  year,
  employeeId,
  bonusBudgetId,
}) {
  const [bonusValue, setBonusValue] = useState(0);

  const {
    register,
    formState: { errors },
    handleSubmit,
  } = useForm();

  const mutationAddBonus = useMutation((formData) => {
    const bonus = {
      month: formData.month,
      value: bonusValue,
      dateGenerated: new Date().toISOString().slice(0, 10),
      employeeId: employeeId,
      year: year,
      bonusBudgetId: bonusBudgetId,
    };
    addBonus(bonus);
  });

  const handleAddBonus = async (formData) => {
    mutationAddBonus.mutate(formData);
  };

  if (mutationAddBonus.isLoading) {
    return <LoadingComponent />;
  }

  if (mutationAddBonus.isError) {
    return <p>Wystąpił błąd. Spróbuj ponownie.</p>;
  }

  if (mutationAddBonus.isSuccess) {
    setTimeout(function () {
      onHide();
    }, 1000);
    return <LoadingComponent />;
  }

  return (
    <>
      <Form onSubmit={handleSubmit(handleAddBonus)}>
        <Form.Group>
          <Form.Label htmlFor="month">Miesiąc:</Form.Label>
          <Form.Control
            as="select"
            type="select"
            name="month"
            placeholder="Podaj miesiąc"
            {...register("month", {
              required: true,
            })}
          >
            <option label="Styczeń">01</option>
            <option label="Luty">02</option>
            <option label="Marzec">03</option>
            <option label="Kwiecień">04</option>
            <option label="Maj">05</option>
            <option label="Czerwiec">06</option>
            <option label="Lipiec">07</option>
            <option label="Sierpień">08</option>
            <option label="Wrzesień">09</option>
            <option label="Październik">10</option>
            <option label="Listopad">11</option>
            <option label="Grudzień">12</option>
          </Form.Control>
          {errors.month && errors.month.type === "required" && (
            <FormInputErrorMessage errorMessage="To pole jest wymagane" />
          )}
        </Form.Group>
        <Form.Group>
          <Form.Label htmlFor="value">Wysokość premii (zł):</Form.Label>
          <div style={{ display: "flex", alignItems: "center" }}>
            <div style={{ width: "100%" }} className="mr-3">
              <RangeSlider
                value={bonusValue}
                onChange={(changeEvent) =>
                  setBonusValue(changeEvent.target.value)
                }
                max={5000} //todo: fetch max from api (remaining bonus budget)
              />
            </div>
            <Form.Control
              value={bonusValue}
              onChange={(changeEvent) =>
                setBonusValue(changeEvent.target.value)
              }
              type="number"
              name="value"
            />
          </div>
        </Form.Group>
        <div className="mt-4">
          <Button variant="primary" type="submit" className="mr-3">
            Przydziel premię
          </Button>
          <Button variant="ternary" onClick={onHide}>
            Anuluj
          </Button>
        </div>
      </Form>
    </>
  );
}
