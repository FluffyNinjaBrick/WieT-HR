import "./App.css";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Redirect,
} from "react-router-dom";
import React from "react";
import Navigation from "./components/navigation/Navigation";
import LeavesViewAdmin from "./components/leave/LeavesViewAdmin";
import LeavesView from "./components/leave/LeavesView";
import DelegationsView from "./components/delegation/DelegationsView";
import Profile from "./components/profile/Profile";
import ContractView from "./components/contract/ContractView";
import BonusesView from "./components/bonus/BonusesView";
import BonusesViewAdmin from "./components/bonus/BonusesViewAdmin";
import EmployeeListViewAdmin from "./components/employee/EmployeeListViewAdmin";
import EmployeeEditView from "./components/employee/EmployeeEditView";
import LoginView from "./components/auth/LoginView";
import EmployeeCreateView from "./components/employee/EmployeeCreateView";
import LeaveCreateForm from "./components/leave/LeaveCreateForm";
import DelegationCreateForm from "./components/delegation/DelegationCreateForm";
import DelegationsAdminView from "./components/delegation/DelegationsAdminView";
import RequestsView from "./components/requests/RequestsView";
import SalariesViewAdmin from "./components/salaries/SalariesViewAdmin";
import { ProvideAuth } from "./components/auth/useAuth";
import PrivateRoute from "./components/utils/PrivateRoute";
import "font-awesome/css/font-awesome.min.css";
import { QueryClient, QueryClientProvider } from "react-query";
import ChangeBonusView from "./components/bonus/ChangeBonusView";
import AddContractView from "./components/contract/AddContractView";

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <ProvideAuth>
        <Router>
          <div>
            <Navigation />
            <Switch>
              <PrivateRoute exact path="/" role="any">
                <Redirect to="/profile" />
              </PrivateRoute>
              <PrivateRoute path="/profile" role="any">
                <Profile />
              </PrivateRoute>
              <PrivateRoute path="/delegations/add" role="any">
                <DelegationCreateForm />
              </PrivateRoute>
              <PrivateRoute path="/delegations" role="any">
                <DelegationsView />
              </PrivateRoute>
              <PrivateRoute exact path="/employees" role="ADMIN">
                <EmployeeListViewAdmin />
              </PrivateRoute>
              <PrivateRoute exact path="/employees/requests" role="ADMIN">
                <RequestsView />
              </PrivateRoute>
              <PrivateRoute path="/employees/edit/:id" role="ADMIN">
                <EmployeeEditView />
              </PrivateRoute>
              <PrivateRoute path="/employees/create" role="ADMIN">
                <EmployeeCreateView />
              </PrivateRoute>
              <PrivateRoute path="/employees/delegations" role="ADMIN">
                <DelegationsAdminView />
              </PrivateRoute>
              <PrivateRoute path="/employees/leaves" role="ADMIN">
                <LeavesViewAdmin />
              </PrivateRoute>
              <PrivateRoute path="/employees/bonuses" role="ADMIN">
                <BonusesViewAdmin />
              </PrivateRoute>
              <PrivateRoute path="/employees/salaries" role="ADMIN">
                <SalariesViewAdmin />
              </PrivateRoute>
              <PrivateRoute path="/leaves/add" role="any">
                <LeaveCreateForm />
              </PrivateRoute>
              <PrivateRoute path="/leaves" role="any">
                <LeavesView />
              </PrivateRoute>
              <PrivateRoute path="/contract/add" role="any">
                <AddContractView />
              </PrivateRoute>
              <PrivateRoute path="/contract" role="any">
                <ContractView />
              </PrivateRoute>
              <PrivateRoute path="/bonuses" role="any">
                <BonusesView />
              </PrivateRoute>
              <PrivateRoute path="/employees/changeBonus" role="any">
                <ChangeBonusView />
              </PrivateRoute>
              <Route path="/login">
                <LoginView />
              </Route>
            </Switch>
          </div>
        </Router>
      </ProvideAuth>
    </QueryClientProvider>
  );
}

export default App;
