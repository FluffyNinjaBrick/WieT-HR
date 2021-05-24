import { DatePickerContainer, ViewContainer } from "../../styled";
import { Form } from "react-bootstrap";
import { useState } from "react";
import TableSalaries from "./TableSalaries";

export default function SalariesViewAdmin() {
  const [year, setYear] = useState(new Date().getFullYear());

  const handleYearChange = (e) => {
    console.log(e.target.value);
    setYear(e.target.value);
  };

  return (
    <ViewContainer>
      <h1 className="my-3">Pracownicy / Wynagrodzenie</h1>
      <DatePickerContainer className="my-3">
        <h4 className="m-0 mr-2">Rok: </h4>
        <div>
          <Form.Control
            onChange={handleYearChange}
            as="select"
            defaultValue={new Date().getFullYear()}
          >
            <option>{new Date().getFullYear()}</option>
            <option>{new Date().getFullYear() - 1}</option>
          </Form.Control>
        </div>
      </DatePickerContainer>
      <TableSalaries year={year} />
    </ViewContainer>
  );
}
