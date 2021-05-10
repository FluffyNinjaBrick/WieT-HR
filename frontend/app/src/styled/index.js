import styled from "styled-components";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

//views
export const ViewContainer = styled.div`
  width: 90vw;
  margin: 0 auto;
`;
//

//tables
export const TableContainer = styled.div`
  width: 100%;
`;

export const StyledTable = styled.table`
  width: 100%;
  border-spacing: 0;

  border: 1px solid #dee2e6;
  border-radius: 15px;
  border-collapse: collapse;

  tr {
    :last-child {
      td {
        border-bottom: 0;
      }
    }

    :hover {
      background: #f2f2f2;
    }
  }

  th,
  td {
    margin: 0;
    padding: 0.5rem;
    border-bottom: 1px solid #dee2e6;
    border-right: 1px solid #dee2e6;

    :last-child {
      border-right: 0;
    }
  }

  th {
    background: #f3f3f3;
  }

  // tr:nth-child(even) {
  //   background-color: #f9f9f9;
  // }

  tfoot {
    tr:first-child {
      td {
        border-top: 2px solid black;
      }
    }

    font-weight: bolder;
  }
`;

export const TableSearchInput = styled.input`
  border: 0;
  padding: 0.2em 0.4em;

  &:focus {
    outline: none;
  }
`;

export const Icon = styled(FontAwesomeIcon)`
  margin-left: 6px;
  font-size: 0.8em;
`;
//

//other
export const DatePickerContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
`;
//
