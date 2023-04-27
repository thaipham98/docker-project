import { useMutation } from "@tanstack/react-query";
import { API_URL } from "./const";

// This hook is used to checkout.
export default function useCheckout(handleSuccess = () => {}) {
  return useMutation(
    async () => {
      const response = await fetch(`${API_URL}/checkout`, {
       headers: {
                Accept: "application/json",
                "Content-Type": "application/json",
              },
              method: "PUT",
      });
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      return response;
    },
    {
      onSuccess: handleSuccess,
    }
  );
}
