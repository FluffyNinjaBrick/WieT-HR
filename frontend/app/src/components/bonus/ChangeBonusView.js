import { useQuery, useMutation } from "react-query";
import { useState, useEffect } from "react";
import {
  fetchBonusBudgetForYear,
  changeBonusBudgetForYear,
} from "../../services/EmployeeService";
import FormInputErrorMessage from "../utils/FormInputErrorMessage";
import Form from "react-bootstrap/Form";
import { useForm } from "react-hook-form";
import Button from "react-bootstrap/Button";

export default function ChangeBonusView() {
  const [budgetLeft, setBudgetLeft] = useState(0);
  const [budgetSize, setBudgetSize] = useState(0);
  const [budget, setBudget] = useState(null);
  // const [modifiedBudget, setModifiedBudget] = useState(null);

  const {
    register,
    formState: { errors },
    handleSubmit,
  } = useForm();

  const {
    isLoading,
    error,
    data: apiResponse,
    isFetching,
    refetch,
  } = useQuery("bonusBudget", () =>
    fetchBonusBudgetForYear(new Date().getFullYear())
  );

  useEffect(() => {
    setBudgetLeft(apiResponse?.data?.budgetLeft);
    setBudgetSize(apiResponse?.data?.budgetSize);
    setBudget(apiResponse?.data);
    console.log(budget);
    console.log(budgetLeft + "   " + budgetSize);
  }, [apiResponse]);

  const mutation = useMutation((formData) =>
    changeBonusBudgetForYear(new Date().getFullYear(), {
      ...budget,
      budgetSize: Number(formData.newBudget),
      budgetLeft:
        Number(formData.newBudget) - (budget.budgetSize - budget.budgetLeft),
    })
  );

  const changeBudget = (formData) => {
    console.log(formData);
    // console.log("zmieniammm");
    // changeBonusBudgetForYear(new Date().getFullYear(), formData.newBudget);//TODO zrobic na axios
    mutation.mutate(formData);
  };

  if (isLoading) {
    return <div></div>;
  }
  return (
    <div className="container col-lg-8 col-md-10 col-sm-12 my-5">
      <h3>Zmień roczny budżet premiowy</h3>
      <h5>Aktualny budżet: {budgetSize}</h5>
      <Form onSubmit={handleSubmit(changeBudget)}>
        <Form.Group>
          <Form.Label htmlFor="newBudget">Nowy budżet</Form.Label>
          <Form.Control
            type="text"
            name="newBudget"
            placeholder="Podaj wielkość nowego budżetu"
            defaultValue={budgetSize}
            {...register("newBudget", { required: true })}
          />
          {errors.newBudget && (
            <FormInputErrorMessage errorMessage="Podanie budżetu jest wymagane" />
          )}
        </Form.Group>
        {mutation.isError ? (
          <div>Błąd: nie można zmienić budżetu.</div>
        ) : (
          <div></div>
        )}
        <div>
          <Button className="mt-5 mr-3" variant="primary" type="submit">
            Zapisz nowy budżet
          </Button>
          <Button
            className="mt-5"
            variant="secondary"
            onClick={() => changeBudget()}
          >
            Anuluj
          </Button>
        </div>
      </Form>
    </div>
  );
}
