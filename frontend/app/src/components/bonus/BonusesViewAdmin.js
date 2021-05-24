import TableBonuses from "./TableBonuses";
import { DatePickerContainer, ViewContainer } from "../../styled";
import { Form } from "react-bootstrap";
import { useState } from "react";
import ChangeBonusView from "./ChangeBonusView";
import BonusBudgetStatistics from "./BonusBudgetStatistics";
import AddBonusModal from "./addBonus/AddBonusModal";

export default function BonusesViewAdmin() {
  const [year, setYear] = useState(new Date().getFullYear());

  const handleYearChange = (e) => {
    console.log(e.target.value);
    setYear(e.target.value);
  };

  return (
    <ViewContainer>
      <h1 className="my-3">Pracownicy / premie</h1>
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
      <BonusBudgetStatistics year={year} />
      <TableBonuses year={year} />
      <AddBonusModal />
      {/* <ChangeBonusView /> */}
    </ViewContainer>
  );
}
