import { useEffect, useState } from "react";
import { useQuery, QueryClient } from "react-query";
import { fetchContract } from "../../services/DocumentsService";
import { StyledTableContract } from "../../styled";
import { useAuth } from "../auth/useAuth";
import { Loading, LoadingComponent } from "../loader/LoadingView";

export default function ContractView() {
  const [contract, setContract] = useState(null);

  let auth = useAuth();

  const queryClient = new QueryClient({
    defaultOptions: {
      queries: {
        staleTime: Infinity,
      },
    },
  });

  const {
    isLoading,
    error,
    data: apiResponse,
    refetch,
    isFetching,
    status,
  } = useQuery("contract", () => fetchContract(auth.user.id));

  useEffect(() => {
    queryClient.clear();
    setContract(null);
  }, []);

  useEffect(() => {
    setContract(null);
    queryClient.clear();
    console.log(apiResponse);
    console.log(apiResponse?.data);
    setContract(apiResponse?.data);
  }, [apiResponse]);

  if (isLoading) {
    return <Loading />;
  }

  function getContractTranslation(type) {
    switch (type) {
      case "MANDATE":
        return "Umowa zlecenie";
      case "EMPLOYMENT":
        return "Umowa o pracę";
      case "WORK":
        return "Umowa o dzieło";
      default:
        return "Nieznana";
    }
  }

  return (
    <div className="container">
      <h1 className="my-3">Twoja umowa</h1>
      {!contract ? (
        <p>Nie znaleziono umowy.</p>
      ) : (
        <>
          <StyledTableContract
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
                <td>Zarobki (brutto/msc.)</td>
                <td>{contract.salary} zł</td>
              </tr>
              <tr>
                <td>Godziny pracy w miesiącu</td>
                <td>{contract.workingHours}</td>
              </tr>
              <tr>
                <td>Wysługa lat</td>
                <td>{contract.dutyAllowance} zł</td>
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
          </StyledTableContract>
        </>
      )}
    </div>
  );
}
