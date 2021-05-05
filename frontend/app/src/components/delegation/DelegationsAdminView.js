import { useEffect, useState } from "react";
import BootstrapTable from "react-bootstrap-table-next";
import filterFactory, {
  textFilter,
  selectFilter,
  dateFilter,
  Comparator,
} from "react-bootstrap-table2-filter";
import { Loading } from "../loader/LoadingView";
import { fetchAllDelegations } from "../../services/DocumentsService";

export default function DelegationsAdminView() {
  const [allDelegations, setAllDelegations] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    fetchAllDelegations()
      .then((data) => {
        setAllDelegations(data.filter((x) => x.signed));
      })
      .then(() => setLoading(false));
  }, []);

  const columns = [
    {
      dataField: "name",
      text: "Imię i nazwisko",
      filter: textFilter({
        placeholder: "Podaj imie i nazwisko",
      }),
      headerStyle: { textAlign: "center" },
    },
    {
      dataField: "dateFrom",
      text: "Data od",
      filter: dateFilter({
        withoutEmptyComparatorOption: true,
        comparators: [Comparator.GE],
        comparatorStyle: { display: "none" },
        dateStyle: { position: "relative" },
      }),
      headerStyle: { textAlign: "center" },
    },
    {
      dataField: "dateTo",
      text: "Data do",
      filter: dateFilter({
        withoutEmptyComparatorOption: true,
        comparators: [Comparator.LE],
        comparatorStyle: { display: "none" },
        dateStyle: { position: "relative" },
      }),
      headerStyle: { textAlign: "center" },
    },
    {
      dataField: "destination",
      text: "Miejsce",
      filter: textFilter({
        placeholder: "Podaj miejsce delegacji",
      }),
      headerStyle: { textAlign: "center" },
    },
    {
      dataField: "status",
      text: "Status",
      filter: selectFilter({
        options: {
          Zaakceptowany: "Zaakceptowany",
          Oczekujący: "Oczekujący",
        },
        placeholder: "Wszystkie",
      }),
      headerStyle: { textAlign: "center" },
    },
  ];

  if (loading) {
    return <Loading />;
  }

  return (
    <div className="container justify-content-sm-center">
      <h1 className="my-3">Pracownicy / delegacje</h1>
      <h4 className="my-4">Archiwalne wnioski o delegację</h4>
      {loading ? (
        <Loading />
      ) : (
        <div>
          {allDelegations.length ? (
            <div className="my-3">
              <BootstrapTable
                keyField="id"
                data={allDelegations.map((delegation) => {
                  let x = {
                    name: delegation.nameAtSigning,
                    dateFrom: delegation.dateFrom,
                    dateTo: delegation.dateTo,
                    destination: delegation.destination,
                    status: delegation.signed ? "Zaakceptowany" : "Oczekujący",
                  };
                  return x;
                })}
                columns={columns}
                filter={filterFactory()}
                filterPosition="top"
                striped
                hover
              />
            </div>
          ) : (
            <div className="my-3">
              <h6>Nie znaleziono żadnych wniosków o delegację.</h6>
            </div>
          )}
        </div>
      )}
    </div>
  );
}
