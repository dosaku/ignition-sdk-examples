/*******************************************************************************
 * INDUCTIVE AUTOMATION PUBLIC LICENSE 
 * 
 * BY DOWNLOADING, INSTALLING AND/OR IMPLEMENTING THIS SOFTWARE YOU AGREE 
 * TO THE FOLLOWING LICENSE: 
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are 
 * met: 
 * 
 * Redistributions of source code must retain the above copyright notice, 
 * this list of conditions and the following disclaimer. Redistributions in 
 * binary form must reproduce the above copyright notice, this list of 
 * conditions and the following disclaimer in the documentation and/or 
 * other materials provided with the distribution. Neither the name of 
 * Inductive Automation nor the names of its contributors may be used to 
 * endorse or promote products derived from this software without specific 
 * prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS 
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED 
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A 
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL INDUCTIVE 
 * AUTOMATION BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 * 
 * LICENSEE SHALL INDEMNIFY, DEFEND AND HOLD HARMLESS INDUCTIVE AUTOMATION, 
 * ITS SHAREHOLDERS, OFFICERS, DIRECTORS, EMPLOYEES, AGENTS, ATTORNEYS, 
 * SUCCESSORS AND ASSIGNS FROM ANY AND ALL claims, debts, liabilities, 
 * demands, suits and causes of action, known or unknown, in any way 
 * relating to the LICENSEE'S USE OF THE SOFTWARE IN ANY FORM OR MANNER
 * WHATSOEVER AND FOR any act or omission related thereto.
 ******************************************************************************/
package com.inductiveautomation.xopc.drivers.modbus2.requests.util;

import java.util.Iterator;
import java.util.List;

import com.inductiveautomation.xopc.driver.api.items.DriverItem;
import com.inductiveautomation.xopc.drivers.modbus2.address.ModbusAddress;

public class RequestOffsets {

	private final int startAddress;
	private final int endAddress;
	private final int length;

	private RequestOffsets(int startAddress, int endAddress, int length) {
		this.startAddress = startAddress;
		this.endAddress = endAddress;
		this.length = length;
	}

	public int getStartAddress() {
		return startAddress;
	}

	public int getEndAddress() {
		return endAddress;
	}

	public int getLength() {
		return length;
	}

	public static class Calculator {

		private final List<? extends DriverItem> items;

		public Calculator(List<? extends DriverItem> items) {
			this.items = items;
		}

		public RequestOffsets calculate() {
			Iterator<? extends DriverItem> iter = items.iterator();

			DriverItem item = iter.next();
			ModbusAddress address = (ModbusAddress) item.getAddressObject();

			int startAddress = address.getStartAddress();
			int endAddress = calculateEndAddress(address);
			int length = address.getAddressSpan();

			while (iter.hasNext()) {
				DriverItem nextItem = iter.next();
				ModbusAddress nextAddress = (ModbusAddress) nextItem.getAddressObject();

				endAddress = Math.max(endAddress, calculateEndAddress(nextAddress));
				length = Math.max(length, (endAddress - startAddress) + 1);
			}

			return new RequestOffsets(startAddress, endAddress, length);
		}

		private int calculateEndAddress(ModbusAddress address) {
			int span = address.getAddressSpan();
			return address.getStartAddress() + (span - 1);
		}

	}

}
