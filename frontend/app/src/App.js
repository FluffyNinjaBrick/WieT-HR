import "./App.css";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Redirect,
  Link,
} from "react-router-dom";
import React, { useContext, createContext, useState } from "react";
import Navigation from "./components/navigation/Navigation";
import LeavesViewAdmin from "./components/leave/LeavesViewAdmin";
import LeavesView from "./components/leave/LeavesView";
import DelegationsView from "./components/delegation/DelegationsView";
import Profile from "./components/profile/Profile";
import ContractView from "./components/contract/ContractView";
import BonusesView from "./components/bonus/BonusesView";
import EmployeeListViewAdmin from "./components/employee/EmployeeListViewAdmin";
import EmployeeEditView from "./components/employee/EmployeeEditView";
import LoginView from "./components/auth/LoginView";
import EmployeeCreateView from "./components/employee/EmployeeCreateView";
import LeaveCreateForm from "./components/leave/LeaveCreateForm";
import DelegationCreateForm from "./components/delegation/DelegationCreateForm";
import { ProvideAuth } from "./components/auth/useAuth";
import PrivateRoute from "./components/utils/PrivateRoute";

function App() {
  return (
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
            <PrivateRoute path="/employees/edit/:id" role="ADMIN">
              <EmployeeEditView />
            </PrivateRoute>
            <PrivateRoute path="/employees/create" role="ADMIN">
              <EmployeeCreateView />
            </PrivateRoute>
            {/* tutaj trzeba bedzie zrobic routy dla urlopu pracownika i urlopow wszystkich 
          pracownikow, jak sie maprawa do ich wyswietlania, dropdown pracownicy -> urlopy */}
            <PrivateRoute path="/employees/leaves" role="ADMIN">
              <LeavesViewAdmin />
            </PrivateRoute>
            <PrivateRoute path="/leaves/add" role="any">
              <LeaveCreateForm />
            </PrivateRoute>
            <PrivateRoute path="/leaves" role="any">
              <LeavesView />
            </PrivateRoute>
            <PrivateRoute path="/contract" role="any">
              <ContractView />
            </PrivateRoute>
            <PrivateRoute path="/bonuses" role="any">
              <BonusesView />
            </PrivateRoute>
            <Route path="/login">
              <LoginView />
            </Route>
          </Switch>
        </div>
      </Router>
    </ProvideAuth>
  );
}

export default App;
