package com.chdlsp.pokeball.interfaces;

import com.chdlsp.pokeball.model.network.Header;

public interface CrudInterface<ApiRequest, ApiResponse> {
    Header<ApiResponse> create(Header<ApiRequest> ApiRequest);
    Header<ApiResponse> read(Long id);
    Header<ApiResponse> update(Header<ApiRequest> ApiRequest);
    Header delete(Long id);
}
