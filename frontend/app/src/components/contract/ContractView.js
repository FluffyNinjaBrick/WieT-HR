import { useEffect, useState } from "react";
import { fetchContract } from "../../services/DocumentsService";
import "../../styled/ContractTableStyle.css";
import { LoadingComponent } from "../loader/LoadingView";

export default function ContractView() {
  const [contract, setContract] = useState({});

  useEffect(() => {
    let responseContract = fetchContract();
    responseContract
      .then((result) => {
        setContract(result.data[0]);
        console.log(result.data[0]);
      })
      .catch((error) => console.log(error));
  }, []);

  function getContractTranslation(type) {
    switch (type) {
      case "MANDATE":
        return "Zlecenie";
      case "EMPLOYMENT":
        return "O pracę";
      case "WORK":
        return "O dzieło";
      default:
        return "Nieznana";
    }
  }

  if (contract === {}) {
    return <LoadingComponent />;
  }
  return (
    <div className="container">
      <h1 className="my-3">Umowa</h1>
      <table
        style={
          (new Date(contract.dateTo).getTime() - new Date().getTime()) /
            (1000 * 60 * 60 * 24) <
          30
            ? { border: "3px solid red" }
            : {}
        }
      >
        <tbody>
          <tr>
            <td>Imię i nazwisko</td>
            <td>{contract.nameAtSigning}</td>
          </tr>
          <tr>
            <td>Data rozpoczęcia</td>
            <td>{contract.dateFrom}</td>
          </tr>
          <tr>
            <td>Data wygaśnięcia</td>
            <td>{contract.dateTo}</td>
          </tr>
          <tr>
            <td>Data wydania</td>
            <td>{contract.dateIssued}</td>
          </tr>
          <tr>
            <td>Data podpisania</td>
            <td>{contract.dateSigned}</td>
          </tr>
          <tr>
            <td>Zarobki</td>
            <td>{contract.salary}</td>
          </tr>
          <tr>
            <td>Godziny pracy</td>
            <td>{contract.workingHours}</td>
          </tr>
          <tr>
            <td>Dopłata celna</td>
            <td>{contract.dutyAllowance}</td>
          </tr>
          <tr>
            <td>Dni wolne w roku</td>
            <td>{contract.annualLeaveDays}</td>
          </tr>
          <tr>
            <td>Typ umowy</td>
            <td>{getContractTranslation(contract.type)}</td>
          </tr>
        </tbody>
      </table>
    </div>
  );
}
