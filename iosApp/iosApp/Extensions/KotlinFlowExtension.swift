//
//  KotlinFlowExtension.swift
//  iosApp
//
//  Created by Administrator on 27.09.2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import shared




class FlowCollector<T : CoreViewState> : Kotlinx_coroutines_coreFlowCollector{
    
    let callback : (T) -> Void
    
    init(callback : @escaping (T) -> Void) {
        self.callback = callback
    }
    
    func emit(value: Any?, completionHandler: @escaping (KotlinUnit?, Error?) -> Void) {
        callback(value as! T)
        completionHandler(KotlinUnit(), nil)
    }
    
    
}


extension  Kotlinx_coroutines_coreStateFlow{
    func observe<T : CoreViewState>(value : @escaping (T) -> Void) -> Void {
        collect(collector: FlowCollector<T>{ v in
            value(v)
        }, completionHandler: {(u, e) in
            
        })
    }
}

extension  Kotlinx_coroutines_coreSharedFlow{
    func observe<T : CoreViewState>(value : @escaping (T) -> Void) -> Void {
        collect(collector: FlowCollector<T>{ v in
            value(v)
        }, completionHandler: {(u, e) in
            
        })
    }
}


